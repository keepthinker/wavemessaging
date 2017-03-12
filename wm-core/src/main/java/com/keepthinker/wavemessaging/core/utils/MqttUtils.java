package com.keepthinker.wavemessaging.core.utils;

import io.netty.handler.codec.mqtt.*;

public class MqttUtils {

    public static final MqttMessage PINGREQ = new MqttMessage(new MqttFixedHeader
            (MqttMessageType.PINGREQ, false, MqttQoS.AT_MOST_ONCE, false, 0));


    public static final MqttMessage PINGRESP = new MqttMessage(new MqttFixedHeader
            (MqttMessageType.PINGRESP, false, MqttQoS.AT_MOST_ONCE, false, 0));
    public static final MqttConnAckMessage CONNACK_ACCEPTED_MESSAGE;
    public static final MqttConnAckMessage CONNACK_REFUSED_INVALID_TOKEN;
    public static final MqttConnAckMessage CONNECTION_REFUSED_IDENTIFIER_REJECTED_MESSAGE;

    static {

        CONNACK_ACCEPTED_MESSAGE = createHandlerConnAckMessage(MqttConnectReturnCode.CONNECTION_ACCEPTED);
        CONNACK_REFUSED_INVALID_TOKEN = createHandlerConnAckMessage(MqttConnectReturnCode.CONNECTION_REFUSED_NOT_AUTHORIZED);
        CONNECTION_REFUSED_IDENTIFIER_REJECTED_MESSAGE = createHandlerConnAckMessage(MqttConnectReturnCode.CONNECTION_REFUSED_IDENTIFIER_REJECTED);
    }

    public static MqttConnAckMessage createHandlerConnAckMessage(MqttConnectReturnCode returnCode) {
        MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(
                MqttMessageType.CONNACK, false, MqttQoS.AT_MOST_ONCE, false, 0);
        MqttConnAckVariableHeader variableHeader = new MqttConnAckVariableHeader(returnCode, true);
        MqttConnAckMessage connAckMessage = new MqttConnAckMessage(mqttFixedHeader, variableHeader);
//        MqttSubAckMessage a = new MqttSubAckMessage();


        return connAckMessage;
    }
}
