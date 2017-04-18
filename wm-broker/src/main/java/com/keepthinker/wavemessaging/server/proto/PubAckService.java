package com.keepthinker.wavemessaging.server.proto;

import com.keepthinker.wavemessaging.core.ProtocolService;
import com.keepthinker.wavemessaging.proto.WmpPubAckMessage;
import com.keepthinker.wavemessaging.server.HandlerChannelMananger;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;

/**
 * Created by keepthinker on 2017/4/16.
 */
public class PubAckService implements ProtocolService<WmpPubAckMessage> {

    private static final Logger LOGGER = LogManager.getLogger();

    @Resource
    private HandlerChannelMananger handlerChannelManager;

    @Override
    public void handle(ChannelHandlerContext ctx, WmpPubAckMessage msg) {
         String clientId = msg.getBody().getClientId();
         if(StringUtils.isNotBlank(clientId) && StringUtils.isNumeric(clientId)) {
            Channel channel = handlerChannelManager.get(clientId);
            channel.writeAndFlush(msg);
        }else{
            LOGGER.warn("illegal client id|{}", msg.getBody().getClientId());
        }
    }
}
