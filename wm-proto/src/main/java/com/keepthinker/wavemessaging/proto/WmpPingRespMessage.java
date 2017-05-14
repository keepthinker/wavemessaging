package com.keepthinker.wavemessaging.proto;

/**
 * Created by keepthinker on 2017/3/19.
 */
public class WmpPingRespMessage extends WmpMessage{

    public WmpPingRespMessage() {
        super.method = WmpMessageMethod.PINGRESP;
    }

    public WmpPingRespMessage(int version) {
        super.method = WmpMessageMethod.PINGRESP;
        super.setVersion(version);
    }
}
