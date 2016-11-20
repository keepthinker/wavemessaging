package com.keepthinker.wavemessaging.server.proto;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.keepthinker.wavemessaging.common.Constants;
import com.keepthinker.wavemessaging.core.ProtocolService;
import com.keepthinker.wavemessaging.server.ChannelManager;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.mqtt.MqttConnectMessage;
import io.netty.handler.codec.mqtt.MqttConnectPayload;
import io.netty.handler.codec.mqtt.MqttConnectVariableHeader;

@Service
public class ConnectService implements ProtocolService<MqttConnectMessage>{

	private static final Logger LOGGER = LogManager.getLogger();
	
	@Autowired
	private ChannelManager channelManager;
	
	@Override
	public void handle(ChannelHandlerContext ctx, MqttConnectMessage msg) {
		LOGGER.info(msg.fixedHeader().messageType());
		MqttConnectVariableHeader variableHeader = msg.variableHeader();
		LOGGER.info(variableHeader);
		MqttConnectPayload payload = msg.payload();
		LOGGER.info(payload);
		
		if(variableHeader.hasUserName() == false){// other nodes(handler etc.) in intranet don't need authentication
			String clientId = payload.clientIdentifier();
			if(clientId.split(":")[0].equals(Constants.NODE_NAME_HANDLER)){
				channelManager.getHandlerChannelMananger().add(ctx.channel());
			}
		}else{
			
		}
		
	}

}
