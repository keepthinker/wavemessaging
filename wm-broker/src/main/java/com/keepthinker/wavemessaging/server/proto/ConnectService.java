package com.keepthinker.wavemessaging.server.proto;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.keepthinker.wavemessaging.core.ProtocolService;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.mqtt.MqttConnectMessage;

@Service
public class ConnectService implements ProtocolService<MqttConnectMessage>{

	private static final Logger LOGGER = LogManager.getLogger();
	
	@Override
	public void handle(ChannelHandlerContext ctx, MqttConnectMessage msg) {
		LOGGER.info(msg.fixedHeader().messageType());
		LOGGER.info(msg.variableHeader());
		LOGGER.info(msg.payload());
	}

}
