package com.keepthinker.wavemessaging.server.proto;

import com.keepthinker.wavemessaging.core.ProtocolService;
import com.keepthinker.wavemessaging.core.utils.MqttUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.mqtt.MqttMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class PingReqService implements ProtocolService<MqttMessage> {
    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void handle(ChannelHandlerContext ctx, MqttMessage msg) {
        LOGGER.info(msg.fixedHeader().messageType());
        ctx.channel().writeAndFlush(MqttUtils.PINGRESP);
    }


}
