package com.keepthinker.wavemessaging.handler.proto;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.keepthinker.wavemessaging.core.ProtocolService;
import com.keepthinker.wavemessaging.handler.ChannelHolder;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.mqtt.MqttMessage;

@Service
public class ConnackService implements ProtocolService<MqttMessage>{
	private static Logger LOGGER = LogManager.getLogger();
	
	@Autowired
	private ChannelHolder holder;
	
	@Override
	public void handle(ChannelHandlerContext ctx, MqttMessage msg) {
		LOGGER.debug("Connect to borker successfully");
		
		holder.add(ctx.channel());
	}

}
