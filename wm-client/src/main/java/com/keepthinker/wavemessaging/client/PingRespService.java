package com.keepthinker.wavemessaging.client;

import org.springframework.stereotype.Service;

import com.keepthinker.wavemessaging.core.ProtocolService;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.mqtt.MqttMessage;

@Service
public class PingRespService implements ProtocolService<MqttMessage>{

	@Override
	public void handle(ChannelHandlerContext ctx, MqttMessage msg) {
		System.out.println(msg.fixedHeader().messageType());
	}

}
