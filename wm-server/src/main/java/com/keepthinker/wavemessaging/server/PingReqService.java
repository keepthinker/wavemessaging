package com.keepthinker.wavemessaging.server;

import org.springframework.stereotype.Service;

import com.keepthinker.wavemessaging.core.MqttUtils;
import com.keepthinker.wavemessaging.core.ProtocolService;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.mqtt.MqttMessage;

@Service
public class PingReqService implements ProtocolService<MqttMessage>{

	@Override
	public void handle(ChannelHandlerContext ctx, MqttMessage msg) {
		System.out.println(msg.fixedHeader().messageType());
    	ctx.channel().writeAndFlush(MqttUtils.getPingRespMessage());
	}


}
