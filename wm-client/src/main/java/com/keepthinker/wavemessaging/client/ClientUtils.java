package com.keepthinker.wavemessaging.client;

import com.keepthinker.wavemessaging.client.utils.Constants;
import com.keepthinker.wavemessaging.proto.WmpConnectMessage;
import com.keepthinker.wavemessaging.proto.WmpMessageProtos;

public class ClientUtils {
    public static WmpConnectMessage createConnectMessage(long clientId, String token) {
        String clientIdentifier = String.valueOf(clientId);

        WmpMessageProtos.WmpConnectMessageBody body = WmpMessageProtos.WmpConnectMessageBody.newBuilder()
                .setClientId(clientIdentifier).setToken(token).build();

        WmpConnectMessage message = new WmpConnectMessage();
        message.setVersion(Constants.WMP_VERSION);
        message.setBody(body);
        return message;
    }
}
