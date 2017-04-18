package com.keepthinker.wavemessaging.proto;

/**
 * Created by keepthinker on 2017/4/16.
 */
public class WmpPubAckMessage extends WmpMessage{

    private WmpMessageProtos.WmpPubAckMessageBody body;

    public WmpMessageProtos.WmpPubAckMessageBody getBody() {
        return body;
    }

    public void setBody(WmpMessageProtos.WmpPubAckMessageBody body) {
        this.body = body;
    }

    public WmpPubAckMessage(){
        super.method = WmpMessageMethod.PUBLISH;
    }
}
