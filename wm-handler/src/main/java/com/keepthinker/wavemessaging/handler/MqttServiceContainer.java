package com.keepthinker.wavemessaging.handler;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.keepthinker.wavemessaging.core.ProtocolService;
import com.keepthinker.wavemessaging.handler.proto.PingRespService;

import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttMessageType;

@Service 
public class MqttServiceContainer {
	
	@Autowired
	private PingRespService pingRespService;
	
	private final Map<MqttMessageType, ProtocolService<MqttMessage>> services = new HashMap<>();
	
	public void put(MqttMessageType type, ProtocolService<MqttMessage> service){
		services.put(type, service);
	}
	
	public ProtocolService<MqttMessage> get(MqttMessageType type){
		return services.get(type);
	}
	
	@PostConstruct
	public void init(){
		services.put(MqttMessageType.PINGRESP, pingRespService);
	}
	
}
