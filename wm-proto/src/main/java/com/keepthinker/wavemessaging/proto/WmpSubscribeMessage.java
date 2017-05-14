package com.keepthinker.wavemessaging.proto;

/**
 * Created by keepthinker on 2017/5/1.
 */
public class WmpSubscribeMessage extends WmpMessage{
    private WmpMessageProtos.WmpSubscribeMessageBody body;

    public WmpMessageProtos.WmpSubscribeMessageBody getBody() {
        return body;
    }

    public void setBody(WmpMessageProtos.WmpSubscribeMessageBody body) {
        this.body = body;
    }

    public WmpSubscribeMessage() {
        super.method = WmpMessageMethod.SUBSCRIBE;
    }
}
