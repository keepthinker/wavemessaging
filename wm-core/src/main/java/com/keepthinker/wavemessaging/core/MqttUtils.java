package com.keepthinker.wavemessaging.core;

import io.netty.handler.codec.mqtt.MqttFixedHeader;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttMessageType;
import io.netty.handler.codec.mqtt.MqttQoS;

public class MqttUtils {
	
	private static final MqttMessage PINGREQU = new MqttMessage(new MqttFixedHeader
			(MqttMessageType.PINGREQ, false, MqttQoS.AT_MOST_ONCE, false, 0));
	
	public static MqttMessage getPingReqMessage(){
		return PINGREQU;
	}
	

	private static final MqttMessage PINGRESP = new MqttMessage(new MqttFixedHeader
			(MqttMessageType.PINGRESP, false, MqttQoS.AT_MOST_ONCE, false, 0));
	
	public static MqttMessage getPingRespMessage(){
		return PINGRESP;
	}
}
