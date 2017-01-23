package com.keepthinker.wavemessaging.handler.proto;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.keepthinker.wavemessaging.core.ProtocolService;
import com.keepthinker.wavemessaging.handler.ChannelHolder;
import com.keepthinker.wavemessaging.handler.utils.HandlerUtils;
import com.keepthinker.wavemessaging.redis.RedisUtils;
import com.keepthinker.wavemessaging.redis.WmStringRedisTemplate;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.mqtt.MqttConnAckMessage;
import io.netty.handler.codec.mqtt.MqttConnectMessage;
import io.netty.handler.codec.mqtt.MqttConnectPayload;
import io.netty.handler.codec.mqtt.MqttConnectVariableHeader;

@Service
public class ConnectService implements ProtocolService<MqttConnectMessage>{
	private static Logger LOGGER = LogManager.getLogger();
	
	@Autowired
	private ChannelHolder holder;
	
	@Autowired
	private WmStringRedisTemplate redisTemplate;
	
	@Override
	public void handle(ChannelHandlerContext ctx, MqttConnectMessage msg) {
		LOGGER.debug("Connack to boroker successfully");
		MqttConnectVariableHeader header = msg.variableHeader();
		if(header.hasUserName() && header.hasPassword()){
			MqttConnectPayload payload = msg.payload();
			String username = payload.userName();
			String password = payload.password();
			
			String clientId = payload.clientIdentifier();
			redisTemplate.get(RedisUtils.getClientIdKey(clientId));
			
			MqttConnAckMessage connAckMessage = HandlerUtils.HANDLER_CONNECTION_ACCEPTED_MESSAGE;
			ctx.writeAndFlush(connAckMessage);
		}else{
			MqttConnAckMessage connAckMessage = HandlerUtils.HANDLER_CONNECTION_REFUSED_BAD_USER_NAME_OR_PASSWORD_MESSAGE;
			ctx.writeAndFlush(connAckMessage);
		}
		
	}

}
