package com.keepthinker.wavemessaging.core.model;

/**
 * Created by keepthinker on 2017/4/4.
 */
public class ClientMessageWaiting {

    private String clientId;

    private long messageId;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

}
