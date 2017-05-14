package com.keepthinker.wavemessaging.core.utils;

import com.keepthinker.wavemessaging.proto.*;
import static com.keepthinker.wavemessaging.proto.WmpMessageProtos.*;

public class WmpUtils {

    public static final WmpPingReqMessage PINGREQ = new WmpPingReqMessage();

    public static final WmpPingRespMessage PINGRESP = new WmpPingRespMessage();

    public static final WmpConnAckMessage CONNACK_ACCEPTED_MESSAGE;
    public static final WmpConnAckMessage CONNACK_REFUSED_INVALID_CLIENT_ID_OR_TOKEN_MESSAGE;
    public static final WmpConnAckMessage CONNECTION_REFUSED_CLIENT_ID_REJECTED_MESSAGE;

    static {

        CONNACK_ACCEPTED_MESSAGE = createHandlerConnAckMessage(ConnectReturnCode.ACCEPTED);
        CONNACK_REFUSED_INVALID_CLIENT_ID_OR_TOKEN_MESSAGE = createHandlerConnAckMessage(ConnectReturnCode.REFUSED_NOT_AUTHORIZED);
        CONNECTION_REFUSED_CLIENT_ID_REJECTED_MESSAGE = createHandlerConnAckMessage(ConnectReturnCode.REFUSED_IDENTIFIER_REJECTED);
    }

    public static WmpConnAckMessage createHandlerConnAckMessage(ConnectReturnCode returnCode) {
        WmpConnAckMessageBody messageBody = WmpConnAckMessageBody
                .newBuilder().setReturnCode(returnCode).build();
        WmpConnAckMessage connAckMessage = new WmpConnAckMessage(messageBody);
        connAckMessage.setVersion(Constants.WMP_VERSION);
        return connAckMessage;
    }

    public static WmpDisconnectMessage createDisConnectMessage(String clientId){
        WmpDisconnectMessage message = new WmpDisconnectMessage();
        WmpDisConnectMessageBody body = WmpDisConnectMessageBody.newBuilder()
                .setClientId(clientId).build();
        message.setBody(body);
        message.setVersion(Constants.WMP_VERSION);
        return message;
    }
}
