package com.keepthinker.wavemessaging.proto;

/**
 * Created by keepthinker on 2017/3/19.
 */
public class WmpConnAckMessage extends WmpMessage{
    private WmpMessageProtos.WmpConnAckMessageBody body;

    public WmpMessageProtos.WmpConnAckMessageBody getBody() {
        return body;
    }

    public void setBody(WmpMessageProtos.WmpConnAckMessageBody body) {
        this.body = body;
    }

    public WmpConnAckMessage(WmpMessageProtos.WmpConnAckMessageBody messageBody) {
        this.body = messageBody;
        super.method = WmpMessageMethod.CONNACK;
    }

    public WmpConnAckMessage() {
        super.method = WmpMessageMethod.CONNACK;
    }

}
