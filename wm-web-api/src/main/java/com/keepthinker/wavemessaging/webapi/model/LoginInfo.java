package com.keepthinker.wavemessaging.webapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginInfo {
    @JsonProperty("u")
    private String username;
    @JsonProperty("p")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
