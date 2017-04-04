package com.keepthinker.wavemessaging.proto;

/**
 * Created by keepthinker on 2017/4/4.
 */
public class WmpDisConnectMessage extends WmpMessage{

    private WmpMessageProtos.WmpDisConnectMessageBody body;

    public WmpMessageProtos.WmpDisConnectMessageBody getBody() {
        return body;
    }

    public void setBody(WmpMessageProtos.WmpDisConnectMessageBody body) {
        this.body = body;
    }

    public WmpDisConnectMessage() {
        super.method = WmpMessageMethod.DISCONNECT;
    }
}
