package com.keepthinker.wavemessaging.server.proto;

import com.google.protobuf.ProtocolStringList;
import com.keepthinker.wavemessaging.core.ProtocolService;
import com.keepthinker.wavemessaging.proto.WmpUnsubscribeMessage;
import com.keepthinker.wavemessaging.server.HandlerChannelMananger;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by keepthinker on 2017/6/17.
 */
@Service
public class UnsubscribeService implements ProtocolService<WmpUnsubscribeMessage> {

    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    private HandlerChannelMananger handlerChannelManager;

    @Override
    public void handle(ChannelHandlerContext ctx, WmpUnsubscribeMessage msg) {
        String clientId = msg.getBody().getClientId();
        ProtocolStringList list =  msg.getBody().getTopicsList();

        if(StringUtils.isNotBlank(clientId) && StringUtils.isNumeric(clientId)
                && list.size() > 0) {
            Channel channel = handlerChannelManager.get(clientId);
            if(channel != null && channel.isActive()) {
                channel .writeAndFlush(msg);
            }else{
                LOGGER.error("channel is null or inactive|channel:{}", channel);
            }
        }else{
            LOGGER.warn("illegal client id|{}", clientId);
        }
    }
}
