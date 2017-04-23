package com.keepthinker.wavemessaging.client.utils;

import com.keepthinker.wavemessaging.proto.WmpMessageProtos;
import com.keepthinker.wavemessaging.proto.WmpPingReqMessage;
import com.keepthinker.wavemessaging.proto.WmpPingRespMessage;
import com.keepthinker.wavemessaging.proto.WmpPubAckMessage;

public class WmpUtils {

    public static final WmpPingReqMessage PINGREQ = new WmpPingReqMessage(Constants.WMP_VERSION);

    public static final WmpPingRespMessage PINGRESP = new WmpPingRespMessage(Constants.WMP_VERSION);

    public static WmpPubAckMessage generatePubAckMsg(String clientId, long messageId){
        WmpMessageProtos.WmpPubAckMessageBody msgBody = WmpMessageProtos.WmpPubAckMessageBody.newBuilder()
                .setClientId(clientId)
                .setMessageId(messageId)
                .build();

        WmpPubAckMessage msg = new WmpPubAckMessage();
        msg.setVersion(Constants.WMP_VERSION);
        msg.setBody(msgBody);
        return msg;
    }
}
