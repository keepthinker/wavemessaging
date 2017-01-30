package com.keepthinker.wavemessaging.client;

import com.keepthinker.wavemessaging.core.utils.WmUtils;
import io.netty.handler.codec.mqtt.*;

public class ClientUtils {
    public static MqttConnectMessage createConnectMessage(String userName, String password, String willTopic) {
        String clientIdentifier = "handler:" + WmUtils.getIPV4Private();
        String willMessage = null;

        //remaining length is calculated in MqttEncoder, here just to emphasize
        //variable header size(10) plus payload size(data length(2) + data(?))
        int remainingLength = 10 + (2 + (2 + clientIdentifier.length()) + (2 + willTopic.length()));

        MqttConnectPayload connectPayload = new
                MqttConnectPayload(clientIdentifier, willTopic, willMessage, userName, password);

        MqttConnectVariableHeader innerAuthedVariableHeader =
                new MqttConnectVariableHeader("MQTT", 4, false, false, true, 0, true, false, 6 * 60);

        MqttFixedHeader connectFixedHeader = new MqttFixedHeader
                (MqttMessageType.CONNECT, false, MqttQoS.AT_MOST_ONCE, false, remainingLength);

        MqttConnectMessage connectMessage = new MqttConnectMessage(connectFixedHeader,
                innerAuthedVariableHeader,
                connectPayload);

        return connectMessage;
    }
}