package com.keepthinker.wavemessaging.proto;

/**
 * Created by keepthinker on 2017/6/17.
 */
public class WmpUnsubscribeMessage extends WmpMessage{

    private WmpMessageProtos.WmpUnsubscribeMessageBody body;

    public WmpMessageProtos.WmpUnsubscribeMessageBody getBody() {
        return body;
    }

    public void setBody(WmpMessageProtos.WmpUnsubscribeMessageBody body) {
        this.body = body;
    }

    public WmpUnsubscribeMessage() {
        super.method = WmpMessageMethod.UNSUBSCRIBE;
    }
}
