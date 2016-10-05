package com.keepthinker.wavemessaging.server;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttMessageType;

@Service 
public class MqttServiceContainer {
	private final Map<MqttMessageType, ProtocolService<MqttMessage>> services = new HashMap<>();
	
	public void put(MqttMessageType type, ProtocolService<MqttMessage> service){
		services.put(type, service);
	}
	
	public ProtocolService<MqttMessage> get(MqttMessageType type){
		return services.get(type);
	}
	
}
