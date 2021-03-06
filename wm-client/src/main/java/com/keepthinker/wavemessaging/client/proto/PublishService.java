package com.keepthinker.wavemessaging.client.proto;

import com.keepthinker.wavemessaging.client.service.impl.MessageServiceImpl;
import com.keepthinker.wavemessaging.client.dao.ClientInfoDao;
import com.keepthinker.wavemessaging.client.utils.WmpUtils;
import com.keepthinker.wavemessaging.proto.WmpPubAckMessage;
import com.keepthinker.wavemessaging.proto.WmpPublishMessage;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by keepthinker on 2017/4/21.
 */
@Service
public class PublishService implements ProtocolService<WmpPublishMessage> {

    private static final Logger LOGGER = org.apache.logging.log4j.LogManager.getLogger();

    @Autowired
    private ClientInfoDao clientInfoDao;

    @Autowired
    private MessageServiceImpl messageAction;
    @Override
    public void handle(ChannelHandlerContext ctx, WmpPublishMessage msg) {
        LOGGER.info("receive PUBLISH message body|{}", msg.getBody());
        messageAction.readMessage(msg.getBody().getContent());
        WmpPubAckMessage pubAckMessage = WmpUtils.generatePubAckMsg(
                String.valueOf(clientInfoDao.get().getClientId())
                , msg.getBody().getMessageId());
        ctx.writeAndFlush(pubAckMessage);
    }

}
