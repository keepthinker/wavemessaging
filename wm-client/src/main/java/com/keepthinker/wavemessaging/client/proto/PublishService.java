package com.keepthinker.wavemessaging.client.proto;

import com.keepthinker.wavemessaging.client.action.MessageAction;
import com.keepthinker.wavemessaging.client.dao.ClientInfoDao;
import com.keepthinker.wavemessaging.client.utils.WmpUtils;
import com.keepthinker.wavemessaging.proto.WmpPubAckMessage;
import com.keepthinker.wavemessaging.proto.WmpPublishMessage;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by keepthinker on 2017/4/21.
 */
@Service
public class PublishService implements ProtocolService<WmpPublishMessage>   {

    @Autowired
    private ClientInfoDao clientInfoDao;

    @Autowired
    private MessageAction messageAction;
    @Override
    public void handle(ChannelHandlerContext ctx, WmpPublishMessage msg) {
        messageAction.readMessage(msg.getBody().getContent());
        WmpPubAckMessage pubAckMessage = WmpUtils.generatePubAckMsg(
                String.valueOf(clientInfoDao.get().getClientId())
                , msg.getBody().getMessageId());
        ctx.writeAndFlush(pubAckMessage);
    }

}
