package com.keepthinker.wavemessaging.server.proto;

import com.keepthinker.wavemessaging.core.ProtocolService;
import com.keepthinker.wavemessaging.proto.WmpPubAckMessage;
import com.keepthinker.wavemessaging.server.HandlerChannelMananger;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by keepthinker on 2017/4/16.
 */
@Service
public class PubAckService implements ProtocolService<WmpPubAckMessage> {

    private static final Logger LOGGER = LogManager.getLogger();

    @Resource
    private HandlerChannelMananger handlerChannelManager;

    @Override
    public void handle(ChannelHandlerContext ctx, WmpPubAckMessage msg) {
         String clientId = msg.getBody().getClientId();
         if(StringUtils.isNotBlank(clientId) && StringUtils.isNumeric(clientId)) {
             Channel channel = handlerChannelManager.get(clientId);
             if(channel != null && channel.isActive()) {
                 channel .writeAndFlush(msg);
             }else{
                 LOGGER.error("channel is null or inactive|channel:{}", channel);
             }
        }else{
            LOGGER.warn("illegal client id|{}", msg.getBody().getClientId());
        }
    }
}
