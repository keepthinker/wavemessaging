package com.keepthinker.wavemessaging.handler.utils;

import com.keepthinker.wavemessaging.core.utils.WmUtils;
import com.keepthinker.wavemessaging.proto.WmpConnAckMessage;
import com.keepthinker.wavemessaging.proto.WmpConnectMessage;
import com.keepthinker.wavemessaging.proto.WmpMessageProtos;

public class HandlerUtils {

    public static final WmpConnectMessage HANDLER_CONNECT_MESSAGE;

    static {
        HANDLER_CONNECT_MESSAGE = createHandlerConnectMessage();

    }

    private static WmpConnectMessage createHandlerConnectMessage() {
        String clientId = "handler:" + WmUtils.getIPV4Private();

        WmpConnectMessage wmpConnectMessage = new WmpConnectMessage();
        WmpMessageProtos.WmpConnectMessageBody messageBody = WmpMessageProtos.WmpConnectMessageBody.newBuilder()
                .setClientId(clientId).build();
        wmpConnectMessage.setBody(messageBody);

        return wmpConnectMessage;
    }

    /**
     * message that tell broker the client's connect request succeeds or fails.
     * @param clientId
     * @return
     */
    public static WmpConnAckMessage createSdkConnAckResultMessage(String clientId, WmpMessageProtos.WmpConnectReturnCode returnCode) {
        WmpConnAckMessage wmpConnAckMessage = new WmpConnAckMessage();
        WmpMessageProtos.WmpConnAckMessageBody messageBody = WmpMessageProtos.WmpConnAckMessageBody.newBuilder()
                .setClientId(clientId).setReturnCode(returnCode).build();
        wmpConnAckMessage.setBody(messageBody);

        return wmpConnAckMessage;
    }
}
