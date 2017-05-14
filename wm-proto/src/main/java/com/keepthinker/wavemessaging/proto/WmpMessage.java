package com.keepthinker.wavemessaging.proto;


/**
 * Created by keepthinker on 2017/3/12.
 */
public class WmpMessage {

    protected WmpMessageMethod method;

    protected int version = 1;

    public WmpMessageMethod getMethod() {
        return method;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
