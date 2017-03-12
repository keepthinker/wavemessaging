package com.keepthinker.wavemessaging.proto;

/**
 * Created by keepthinker on 2017/3/12.
 */
public class WmpMessage {

    private WmpMethod method;
    private Object messageBody;

    public WmpMethod getMethod() {
        return method;
    }

    public void setMethod(WmpMethod method) {
        this.method = method;
    }

    public Object getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(Object messageBody) {
        this.messageBody = messageBody;
    }
}
