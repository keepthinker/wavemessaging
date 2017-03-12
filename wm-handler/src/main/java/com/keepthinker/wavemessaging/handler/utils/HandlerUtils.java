package com.keepthinker.wavemessaging.handler.utils;

import com.keepthinker.wavemessaging.core.utils.WmUtils;
import io.netty.handler.codec.mqtt.*;

public class HandlerUtils {

    public static final MqttConnectMessage HANDLER_CONNECT_MESSAGE;

    static {
        HANDLER_CONNECT_MESSAGE = createHandlerConnectMessage();

    }

    private static MqttConnectMessage createHandlerConnectMessage() {
        String clientIdentifier = "handler:" + WmUtils.getIPV4Private();
        String willTopic = "all";
        String willMessage = null;
        String userName = null;
        String password = null;

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

    /**
     * message that tell broker the client's connect request succeeds or fails.
     *
     * @param clientId
     * @param willMessage result msg
     * @return
     */
    public static MqttConnectMessage createSdkConnectResultMessage(String clientId, String willMessage) {
        String clientIdentifier = clientId;
        String willTopic = "";
        //remaining length is calculated in MqttEncoder, here just to emphasize
        //variable header size(10) plus payload size(data length(2) + data(?))
        int remainingLength = 10 + (2 + (2 + clientIdentifier.length()) + (2 + willTopic.length()));

        MqttConnectPayload connectPayload = new
                MqttConnectPayload(clientIdentifier, willTopic, willMessage, "", "");

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
