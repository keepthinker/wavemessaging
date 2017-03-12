package com.keepthinker.wavemessaging.handler;

import com.keepthinker.wavemessaging.core.ProtocolService;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.mqtt.MqttMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Sharable
@Service
public class ServiceHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOGGER = LogManager.getLogger();
    @Autowired
    private MqttServiceContainer serviceContainer;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        LOGGER.debug(msg);
        MqttMessage m = (MqttMessage) msg;
        ProtocolService<MqttMessage> service = serviceContainer.get(m.fixedHeader().messageType());
        service.handle(ctx, m);
    }

}
