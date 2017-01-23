package com.keepthinker.wavemessaging.core.utils;

import io.netty.handler.codec.mqtt.MqttFixedHeader;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttMessageType;
import io.netty.handler.codec.mqtt.MqttQoS;

public class MqttUtils {
	
	public static final MqttMessage PINGREQ = new MqttMessage(new MqttFixedHeader
			(MqttMessageType.PINGREQ, false, MqttQoS.AT_MOST_ONCE, false, 0));
	

	public static final MqttMessage PINGRESP = new MqttMessage(new MqttFixedHeader
			(MqttMessageType.PINGRESP, false, MqttQoS.AT_MOST_ONCE, false, 0));
	
	
	

}
