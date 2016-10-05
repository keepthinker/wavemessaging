package com.keepthinker.wavemessaging.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttMessageType;

@Service
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