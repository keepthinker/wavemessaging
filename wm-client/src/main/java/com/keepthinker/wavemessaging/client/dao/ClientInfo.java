package com.keepthinker.wavemessaging.client.dao;

/**
 * Created by keepthinker on 2017/1/27.
 */
public class ClientInfo {
    private long clientId;
    private String username;
    private String password;

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

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

    @Override
    public String toString() {
        return "ClientInfo{" +
                "clientId=" + clientId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}

