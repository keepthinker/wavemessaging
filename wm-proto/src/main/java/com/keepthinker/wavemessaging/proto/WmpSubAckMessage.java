package com.keepthinker.wavemessaging.proto;

/**
 * Created by keepthinker on 2017/5/1.
 */
public class WmpSubAckMessage extends WmpMessage{

    private WmpMessageProtos.WmpSubAckMessageBody body;

    public WmpMessageProtos.WmpSubAckMessageBody getBody() {
        return body;
    }

    public void setBody(WmpMessageProtos.WmpSubAckMessageBody body) {
        this.body = body;
    }

    public WmpSubAckMessage() {
        super.method = WmpMessageMethod.SUBACK;
    }
}
