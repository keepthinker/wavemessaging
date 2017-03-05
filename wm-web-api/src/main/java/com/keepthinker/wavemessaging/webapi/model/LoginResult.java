package com.keepthinker.wavemessaging.webapi.model;

/**
 * Created by keepthinker on 2017/3/5.
 */
public class LoginResult {
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LoginResult(String token) {
        this.token = token;
    }
}
