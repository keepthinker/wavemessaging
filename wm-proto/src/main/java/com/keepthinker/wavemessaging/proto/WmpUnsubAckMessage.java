package com.keepthinker.wavemessaging.proto;

/**
 * Created by keepthinker on 2017/6/17.
 */
public class WmpUnsubAckMessage extends WmpMessage{

    private WmpMessageProtos.WmpUnsubAckMessageBody body;

    public WmpMessageProtos.WmpUnsubAckMessageBody getBody() {
        return body;
    }

    public void setBody(WmpMessageProtos.WmpUnsubAckMessageBody body) {
        this.body = body;
    }

    public WmpUnsubAckMessage() {
        super.method = WmpMessageMethod.UNSUBACK;
    }
}
