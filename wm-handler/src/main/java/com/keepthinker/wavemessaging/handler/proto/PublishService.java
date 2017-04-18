package com.keepthinker.wavemessaging.handler.proto;

import com.keepthinker.wavemessaging.core.ProtocolService;
import com.keepthinker.wavemessaging.core.utils.Constants;
import com.keepthinker.wavemessaging.handler.ChannelHolder;
import com.keepthinker.wavemessaging.handler.MessageIdGenerator;
import com.keepthinker.wavemessaging.nosql.ClientInfoNoSqlDao;
import com.keepthinker.wavemessaging.nosql.ClientMessageSendingNoSqlDao;
import com.keepthinker.wavemessaging.nosql.ClientMessageWaitingNoSqlDao;
import com.keepthinker.wavemessaging.nosql.MessageInfoNoSqlDao;
import com.keepthinker.wavemessaging.nosql.redis.RedisUtils;
import com.keepthinker.wavemessaging.nosql.redis.WmStringShardRedisTemplate;
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

/**
 * Created by keepthinker on 2017/4/4.
 */
@Service
public class PublishService implements ProtocolService<WmpPublishMessage> {

    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    private MessageIdGenerator messageIdGenerator;

    @Autowired
    private MessageInfoNoSqlDao messageInfoCacheDao;

    @Autowired
    private ClientInfoNoSqlDao clientInfoCacheDao;

    @Autowired
    private ClientMessageSendingNoSqlDao cmSendingNoSqlDao;
    private ClientMessageWaitingNoSqlDao cmWaitingNoSqlDao;

    @Autowired
    private WmStringShardRedisTemplate shardRedisTemplate;

    @Autowired
    private ChannelHolder channelHolder;

    @Override
    public void handle(ChannelHandlerContext ctx, WmpPublishMessage msg) {
        if(msg.getBody().getDirection() == WmpMessageProtos.Direction.TO_SERVER_HANDLER){
            switch (msg.getBody().getTargetType()){
                case CLIENT_ID: handleClientsPublish(msg); break;
                case TOPIC_GENERAL: break;
                default:LOGGER.warn("Target  type not supported");
            }
        } else if(msg.getBody().getDirection() == WmpMessageProtos.Direction.TO_CLIENT_SDK){

        } else {
            LOGGER.warn("not recognized direction|{}", msg.getBody().getDirection());
        }
    }

    private void handleClientsPublish(WmpPublishMessage msg){
        WmpMessageProtos.WmpPublishMessageBody body = msg.getBody();
        String[] clientIds = StringUtils.split(body.getTarget(),',');
        for(int i = 0; i < clientIds.length; i++){
            //record message info in redis and mysql
            long newMsgId = messageIdGenerator.generate();

            MessageInfo messageInfo = new MessageInfo();
            messageInfo.setId(newMsgId);
            messageInfo.setContent(body.getContent());
            messageInfo.setCreateTime(new Date());
            messageInfo.setTimeout(Constants.MESSAGE_DEFAULT_TIMEOUT);
            messageInfoCacheDao.save(messageInfo);

            //send if online
            boolean isSet =  cmSendingNoSqlDao.setNotExist(clientIds[i], String.valueOf(newMsgId));
            if(isSet) {
                String brokerPrivateAddress = clientInfoCacheDao.getBrokerPrivateAddress(RedisUtils.getClientKey(clientIds[i]));
                Channel brokerChannel = channelHolder.getChannel(brokerPrivateAddress);
                WmpMessageProtos.WmpPublishMessageBody newBody = body.toBuilder()
                        .setTarget(clientIds[i])
                        .setMessageId(messageInfo.getId())
                        .setDirection(WmpMessageProtos.Direction.TO_CLIENT_SDK)
                        .build();
                msg.setBody(newBody);
                if(clientInfoCacheDao.getConnectionStatus(clientIds[i]) ==
                        Constants.CONNECTION_STATUTS_ONLINE) {
                    brokerChannel.writeAndFlush(msg);
                }
            }else {
                cmWaitingNoSqlDao.enqueue(clientIds[i], messageInfo.getId());
            }
        }
    }
}
