package com.keepthinker.wavemessaging.client.utils;

import com.keepthinker.wavemessaging.proto.*;

import static com.keepthinker.wavemessaging.proto.WmpMessageProtos.*;

public class WmpUtils {

    public static final WmpPingReqMessage PINGREQ = new WmpPingReqMessage();

    public static final WmpPingRespMessage PINGRESP = new WmpPingRespMessage();

    public static final WmpConnAckMessage CONNACK_ACCEPTED_MESSAGE;
    public static final WmpConnAckMessage CONNACK_REFUSED_INVALID_TOKEN;
    public static final WmpConnAckMessage CONNECTION_REFUSED_IDENTIFIER_REJECTED_MESSAGE;

    static {
        CONNACK_ACCEPTED_MESSAGE = createHandlerConnAckMessage(WmpConnectReturnCode.ACCEPTED);
        CONNACK_REFUSED_INVALID_TOKEN = createHandlerConnAckMessage(WmpConnectReturnCode.REFUSED_NOT_AUTHORIZED);
        CONNECTION_REFUSED_IDENTIFIER_REJECTED_MESSAGE = createHandlerConnAckMessage(WmpConnectReturnCode.REFUSED_IDENTIFIER_REJECTED);
    }

    public static WmpConnAckMessage createHandlerConnAckMessage(WmpConnectReturnCode returnCode) {
        WmpConnAckMessageBody messageBody = WmpConnAckMessageBody
                .newBuilder().setReturnCode(returnCode).build();
        WmpConnAckMessage connAckMessage = new WmpConnAckMessage(messageBody);
        return connAckMessage;
    }
}
