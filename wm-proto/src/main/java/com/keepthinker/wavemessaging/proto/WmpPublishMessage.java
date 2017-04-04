package com.keepthinker.wavemessaging.proto;

/**
 * Created by keepthinker on 2017/4/2.
 */
public class WmpPublishMessage extends WmpMessage{

    private WmpMessageProtos.WmpPublishMessageBody body;

    public WmpMessageProtos.WmpPublishMessageBody getBody() {
        return body;
    }

    public void setBody(WmpMessageProtos.WmpPublishMessageBody body) {
        this.body = body;
    }

    public WmpPublishMessage(){
        super.method = WmpMessageMethod.PUBLISH;
    }
}
