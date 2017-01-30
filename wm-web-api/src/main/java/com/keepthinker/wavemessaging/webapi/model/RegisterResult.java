package com.keepthinker.wavemessaging.webapi.model;

public class RegisterResult {
    private boolean success;
    private long clientId;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    @Override
    public String toString() {
        return "RegisterResult [success=" + success + ", clientId=" + clientId + "]";
    }

}
