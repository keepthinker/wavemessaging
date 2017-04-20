package com.keepthinker.wavemessaging.handler.proto;

import com.keepthinker.wavemessaging.core.ProtocolService;
import com.keepthinker.wavemessaging.core.utils.Constants;
import com.keepthinker.wavemessaging.core.utils.WmpActionLogger;
import com.keepthinker.wavemessaging.handler.ChannelHolder;
import com.keepthinker.wavemessaging.nosql.ClientInfoNoSqlDao;
import com.keepthinker.wavemessaging.nosql.ClientMessageSendingNoSqlDao;
import com.keepthinker.wavemessaging.nosql.ClientMessageWaitingNoSqlDao;
import com.keepthinker.wavemessaging.nosql.MessageInfoNoSqlDao;
import com.keepthinker.wavemessaging.proto.WmpMessageProtos;
import com.keepthinker.wavemessaging.proto.WmpPubAckMessage;
import com.keepthinker.wavemessaging.proto.WmpPublishMessage;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by keepthinker on 2017/4/16.
 */
@Service
public class PubAckService implements ProtocolService<WmpPubAckMessage> {

    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    private ClientMessageSendingNoSqlDao cmSendingNoSqlDao;

    @Autowired
    private ClientMessageWaitingNoSqlDao cmWaitingNoSqlDao;

    @Autowired
    private MessageInfoNoSqlDao messageInfoNoSqlDao;

    @Autowired
    private WmpActionLogger actionLogger;

    @Autowired
    private ChannelHolder channelHolder;

    @Autowired
    private ClientInfoNoSqlDao clientInfoNoSqlDao;

    @Override
    public void handle(ChannelHandlerContext ctx, WmpPubAckMessage msg) {
        WmpMessageProtos.WmpPubAckMessageBody body = msg.getBody();
        long messageId = body.getMessageId();
        String cmsMessageId = cmSendingNoSqlDao.get(body.getClientId());
        if(StringUtils.isNotBlank(cmsMessageId) && messageId == Long.valueOf(cmsMessageId)){
            cmSendingNoSqlDao.delete(body.getClientId());
            actionLogger.puback(body.getClientId(), body.getMessageId());
            //only if message before is sent successfully, it'll find messageWaiting and send waiting message
            findWaitingMsgAndSend(body.getClientId());
        }else{
            LOGGER.warn("mismatch client id in puback message|puback messageId:{}|clientId:{}| messageSending messageId:{}",
                    body.getMessageId(), body.getClientId(), cmsMessageId);
        }

    }

    /**
     * Clients are supposed to be online if it send back PUBACK message;
     * Find waitting messages and send them recursively, which means publish --> puback --> publish --> puback ...
     * until client message waiting list is empty.
     * @param clientId
     */
    private void findWaitingMsgAndSend(String clientId){
        Long messageId = cmWaitingNoSqlDao.dequeue(clientId);
        if(messageId == null) {
            return;
        }
        WmpPublishMessage publishMessage = new WmpPublishMessage();
        publishMessage.setVersion(Constants.WMP_VERSION);
        WmpMessageProtos.WmpPublishMessageBody body = messageInfoNoSqlDao.getPublishMessageBody(messageId);
        publishMessage.setBody(body);
        channelHolder.getChannel(clientInfoNoSqlDao.getBrokerPrivateAddress(clientId)).
                writeAndFlush(publishMessage);
    }
}
