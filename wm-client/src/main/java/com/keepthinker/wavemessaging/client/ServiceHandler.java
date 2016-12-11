package com.keepthinker.wavemessaging.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.keepthinker.wavemessaging.core.ProtocolService;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.mqtt.MqttMessage;

@Service
public class ServiceHandler extends ChannelInboundHandlerAdapter {
	@Autowired
	private MqttServiceContainer serviceContainer;
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		MqttMessage m = (MqttMessage) msg;
    	ProtocolService<MqttMessage> service = serviceContainer.get(m.fixedHeader().messageType());
    	service.handle(ctx, m);
	}
	
}