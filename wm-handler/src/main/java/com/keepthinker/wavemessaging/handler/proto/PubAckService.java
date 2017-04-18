package com.keepthinker.wavemessaging.handler.proto;

import com.keepthinker.wavemessaging.core.ProtocolService;
import com.keepthinker.wavemessaging.core.utils.WmpActionLogger;
import com.keepthinker.wavemessaging.nosql.ClientMessageSendingNoSqlDao;
import com.keepthinker.wavemessaging.nosql.ClientMessageWaitingNoSqlDao;
import com.keepthinker.wavemessaging.nosql.MessageInfoNoSqlDao;
import com.keepthinker.wavemessaging.proto.WmpMessageProtos;
import com.keepthinker.wavemessaging.proto.WmpPubAckMessage;
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

    @Override
    public void handle(ChannelHandlerContext ctx, WmpPubAckMessage msg) {
        WmpMessageProtos.WmpPubAckMessageBody body = msg.getBody();
        long messageId = body.getMessageId();
        String cmsMessageId = cmSendingNoSqlDao.get(body.getClientId());
        if(StringUtils.isNotBlank(cmsMessageId) && messageId == Long.valueOf(cmsMessageId)){
            cmSendingNoSqlDao.delete(body.getClientId());
            actionLogger.puback(body.getClientId(), body.getMessageId());
            //only if message before is sent successfully, it'll find messageWaiting and send waiting message

        }else{
            LOGGER.warn("mismatch client id in puback message|puback messageId:{}|clientId:{}| messageSending messageId:{}",
                    body.getMessageId(), body.getClientId(), cmsMessageId);
        }

    }

    private void findWaitingMsgAndSend(String clientId){
        long messageId = cmWaitingNoSqlDao.dequeue(clientId);
        messageInfoNoSqlDao.get(messageId);
    }
}
