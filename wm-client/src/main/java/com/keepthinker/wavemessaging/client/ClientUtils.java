package com.keepthinker.wavemessaging.client;

import com.keepthinker.wavemessaging.client.model.ClientWillMessage;
import io.netty.handler.codec.mqtt.*;

public class ClientUtils {
    public static MqttConnectMessage createConnectMessage(long clientId, ClientWillMessage willMessage, String willTopic) {
        String clientIdentifier = String.valueOf(clientId);

        //remaining length is calculated in MqttEncoder, here just to emphasize
        //variable header size(10) plus payload size(data length(2) + data(?))
        int remainingLength = 10 + (2 + (2 + clientIdentifier.length()) + (2 + willTopic.length()));

        MqttConnectPayload connectPayload = new
                MqttConnectPayload(clientIdentifier, willTopic, willMessage.getToken(), "", "");

        MqttConnectVariableHeader variableHeader =
                new MqttConnectVariableHeader("MQTT", 4, false, false, true, 0, true, false, 6 * 60);
        MqttFixedHeader connectFixedHeader = new MqttFixedHeader
                (MqttMessageType.CONNECT, false, MqttQoS.AT_MOST_ONCE, false, remainingLength);

        MqttConnectMessage connectMessage = new MqttConnectMessage(connectFixedHeader,
                variableHeader,
                connectPayload);

        return connectMessage;
    }
}
