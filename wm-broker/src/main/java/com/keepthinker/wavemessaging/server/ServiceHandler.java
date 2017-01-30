package com.keepthinker.wavemessaging.server;

import com.keepthinker.wavemessaging.core.ProtocolService;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.mqtt.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * dispatch different type message to corresponding protocal service.<br/>
 * thread safe
 *
 * @author keepthinker
 */
@Service
@Sharable
public class ServiceHandler extends ChannelInboundHandlerAdapter {

    @Autowired
    private MqttServiceContainer serviceContainer;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        MqttMessage m = (MqttMessage) msg;
        ProtocolService<MqttMessage> service = serviceContainer.get(m.fixedHeader().messageType());
        service.handle(ctx, m);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}