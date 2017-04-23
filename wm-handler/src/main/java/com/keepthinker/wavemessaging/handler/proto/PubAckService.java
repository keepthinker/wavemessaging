package com.keepthinker.wavemessaging.handler.proto;

import com.keepthinker.wavemessaging.core.ProtocolService;
import com.keepthinker.wavemessaging.core.utils.WmpActionLogger;
import com.keepthinker.wavemessaging.handler.utils.MessagePublishHelper;
import com.keepthinker.wavemessaging.nosql.ClientMessageSendingNoSqlDao;
import com.keepthinker.wavemessaging.proto.WmpMessageProtos;
import com.keepthinker.wavemessaging.proto.WmpPubAckMessage;
import io.netty.channel.ChannelHandlerContext;
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
    private MessagePublishHelper messagePublishHelper;

    @Override
    public void handle(ChannelHandlerContext ctx, WmpPubAckMessage msg) {
        WmpMessageProtos.WmpPubAckMessageBody body = msg.getBody();
        long messageId = body.getMessageId();
        Long cmsMessageId = cmSendingNoSqlDao.get(body.getClientId());
        if(cmsMessageId != null && messageId == cmsMessageId){
            cmSendingNoSqlDao.delete(body.getClientId());
            WmpActionLogger.puback(body.getClientId(), body.getMessageId());
            //only if message before is sent successfully, it'll find messageWaiting and send waiting message
            messagePublishHelper.findAvailableMessageToSend(body.getClientId());
        }else{
            LOGGER.warn("mismatch client id in puback message|puback messageId:{}|clientId:{}| messageSending messageId:{}",
                    body.getMessageId(), body.getClientId(), cmsMessageId);
        }

    }

}
