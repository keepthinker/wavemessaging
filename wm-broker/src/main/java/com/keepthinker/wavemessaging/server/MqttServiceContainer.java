package com.keepthinker.wavemessaging.server;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.keepthinker.wavemessaging.core.ProtocolService;
import com.keepthinker.wavemessaging.server.proto.ConnectService;
import com.keepthinker.wavemessaging.server.proto.PingReqService;

import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttMessageType;

@Service 
public class MqttServiceContainer {
	
	@Autowired
	private PingReqService pingReqService;
	
	@Autowired
	private ConnectService connectService;
	
	private final Map<MqttMessageType, ProtocolService<? extends MqttMessage>> services = new HashMap<>();
	
	public void put(MqttMessageType type, ProtocolService<MqttMessage> service){
		services.put(type, service);
	}
	
	@SuppressWarnings("unchecked")
	public ProtocolService<MqttMessage> get(MqttMessageType type){
		return (ProtocolService<MqttMessage>) services.get(type);
	}
	
	@PostConstruct
	public void init(){
		services.put(MqttMessageType.PINGREQ, pingReqService);
		services.put(MqttMessageType.CONNECT, connectService);
	}
	
}
