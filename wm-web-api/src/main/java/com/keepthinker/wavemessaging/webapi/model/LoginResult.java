package com.keepthinker.wavemessaging.webapi.model;

/**
 * Created by keepthinker on 2017/3/5.
 */
public class LoginResult {
    private String token;

    private String clientId;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LoginResult(String clientId, String token) {
        this.clientId = clientId;
        this.token = token;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
