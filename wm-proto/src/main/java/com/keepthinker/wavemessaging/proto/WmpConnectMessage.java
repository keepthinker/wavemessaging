package com.keepthinker.wavemessaging.proto;

/**
 * Created by keepthinker on 2017/3/19.
 */
public class WmpConnectMessage extends WmpMessage{
    private WmpMessageProtos.WmpConnectMessageBody body;

    public WmpMessageProtos.WmpConnectMessageBody getBody() {
        return body;
    }

    public void setBody(WmpMessageProtos.WmpConnectMessageBody body) {
        this.body = body;
    }

    public WmpConnectMessage(){
        this.method = WmpMessageMethod.CONNECT;
    }
}
