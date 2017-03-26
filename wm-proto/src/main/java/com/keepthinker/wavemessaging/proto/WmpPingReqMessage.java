package com.keepthinker.wavemessaging.proto;

/**
 * Created by keepthinker on 2017/3/19.
 */
public class WmpPingReqMessage extends WmpMessage{

    public WmpPingReqMessage(int version) {
        super.method = WmpMessageMethod.PINGREQ;
        super.version = version;
    }
}
