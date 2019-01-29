package com.keepthinker.wavemessaging.handler.proto;

import com.keepthinker.wavemessaging.core.ProtocolService;
import com.keepthinker.wavemessaging.core.utils.Constants;
import com.keepthinker.wavemessaging.handler.ChannelHolder;
import com.keepthinker.wavemessaging.handler.MessageIdGenerator;
import com.keepthinker.wavemessaging.nosql.*;
import com.keepthinker.wavemessaging.nosql.redis.RedisUtils;
import com.keepthinker.wavemessaging.nosql.redis.model.MessageInfo;
import com.keepthinker.wavemessaging.proto.WmpMessageProtos;
import com.keepthinker.wavemessaging.proto.WmpPublishMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by keepthinker on 2017/4/4.
 */
@Service
public class PublishService implements ProtocolService<WmpPublishMessage> {

    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    private MessageIdGenerator messageIdGenerator;
    @Autowired
    private MessageInfoNoSqlDao messageInfoNoSqlDao;
    @Autowired
    private ClientInfoNoSqlDao clientInfoNoSqlDao;
    @Autowired
    private ClientMessageSendingNoSqlDao cmSendingNoSqlDao;
    @Autowired
    private ClientMessageWaitingNoSqlDao cmWaitingNoSqlDao;
    @Autowired
    private ChannelHolder channelHolder;
    @Autowired
    private TopicNoSqlDao topicNoSqlDao;

    @Override
    public void handle(ChannelHandlerContext ctx, WmpPublishMessage msg) {
        if(msg.getBody().getDirection() == WmpMessageProtos.Direction.TO_SERVER_HANDLER){
            switch (msg.getBody().getTargetType()){
                case CLIENT_ID: handleClientsPublish(msg); break;
                case TOPIC_GENERAL: handleTopicPublish(msg); break;
                default:LOGGER.warn("Target  type not supported|{}", msg.getBody().getClientId());
            }
        } else if(msg.getBody().getDirection() == WmpMessageProtos.Direction.TO_CLIENT_SDK){

        } else {
            LOGGER.warn("not recognized direction|{}", msg.getBody().getDirection());
        }
    }

    private void handleTopicPublish(WmpPublishMessage msg) {
        WmpMessageProtos.WmpPublishMessageBody body = msg.getBody();
        String[] topics = StringUtils.split(body.getTarget(),',');

        MessageInfo messageInfo = saveBasicMessageBody(body);
        WmpMessageProtos.WmpPublishMessageBody bodyToClient = msg.getBody().toBuilder()
                .setMessageId(messageInfo.getId())
                .setDirection(WmpMessageProtos.Direction.TO_CLIENT_SDK)
                .build();
        msg.setBody(bodyToClient);

        for(int i = 0; i < topics.length; i++){

            for(int j = 0; j < RedisUtils.TOPIC_BUCKET_SIZE; j++) {
                String topicKey = RedisUtils.getTopicGeneralPrefix(topics[i], j);
                topicNoSqlDao.sscanHandle(topicKey, new TopicPublishSendHandler(msg));
            }

        }

    }

    private class TopicPublishSendHandler implements ScanHandler {
        private WmpPublishMessage msg;

        public TopicPublishSendHandler(WmpPublishMessage msg) {
            this.msg = msg;
        }

        @Override
        public void handle(List<String> clientIds) {
            for(String clientId : clientIds) {
                long messageId = msg.getBody().getMessageId();
                boolean isSet = cmSendingNoSqlDao.setNotExist(clientId, messageId);
                if (isSet) {
                    if (clientInfoNoSqlDao.getConnectionStatus(clientId) == Constants.CONNECTION_STATUTS_ONLINE) {
                        String brokerPrivateAddress = clientInfoNoSqlDao.getBrokerPrivateAddress(clientId);
                        Channel brokerChannel = channelHolder.getChannel(brokerPrivateAddress);
                        brokerChannel.writeAndFlush(msg);
                    }
                } else {
                    cmWaitingNoSqlDao.enqueue(clientId, messageId);
                }
            }
        }
    }

    private MessageInfo saveBasicMessageBody(WmpMessageProtos.WmpPublishMessageBody body){
        //record message info in redis and mysql
        long newMsgId = messageIdGenerator.generate();

        MessageInfo messageInfo = new MessageInfo();
        messageInfo.setId(newMsgId);
        messageInfo.setContent(body.getContent());
        messageInfo.setCreateTime(new Date());
        messageInfo.setTimeout(Constants.MESSAGE_DEFAULT_TIMEOUT);
        messageInfoNoSqlDao.save(messageInfo);
        return messageInfo;
    }

    private void handleClientsPublish(WmpPublishMessage msg){
        WmpMessageProtos.WmpPublishMessageBody body = msg.getBody();
        String[] clientIds = StringUtils.split(body.getTarget(),',');

        MessageInfo messageInfo = saveBasicMessageBody(body);

        for(int i = 0; i < clientIds.length; i++){

            WmpMessageProtos.WmpPublishMessageBody newBody = body.toBuilder()
                    .setTargetType(WmpMessageProtos.TargetType.CLIENT_ID)
                    .setTarget(clientIds[i])
                    .setMessageId(messageInfo.getId())
                    .setDirection(WmpMessageProtos.Direction.TO_CLIENT_SDK)
                    .build();
            msg.setBody(newBody);
            messageInfoNoSqlDao.savePublishMessageBody(newBody);

            //send if online
            boolean isSet =  cmSendingNoSqlDao.setNotExist(clientIds[i], messageInfo.getId());
            if(isSet) {
                if(clientInfoNoSqlDao.getConnectionStatus(clientIds[i]) == Constants.CONNECTION_STATUTS_ONLINE){
                    String brokerPrivateAddress = clientInfoNoSqlDao.getBrokerPrivateAddress(clientIds[i]);
                    Channel brokerChannel = channelHolder.getChannel(brokerPrivateAddress);
                    brokerChannel.writeAndFlush(msg);
                }
            }else {
                cmWaitingNoSqlDao.enqueue(clientIds[i], messageInfo.getId());
            }
        }
    }
}
