package com.keepthinker.wavemessaging.nosql.redis.model;

import java.util.Date;

/**
 * Created by keepthinker on 2017/4/9.
 */
public class ClientInfo {

    private String username;
    private String password;
    private long clientId;
    private Integer connectionStatus;
    private String brokerPublicAddress;
    private String brokerPrivateAddress;

    private Date accessTime;
    private Date disconnectTime;

    public Date getAccessTime() {
        return accessTime;
    }

    public void setAccessTime(Date accessTime) {
        this.accessTime = accessTime;
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

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public Integer getConnectionStatus() {
        return connectionStatus;
    }

    public void setConnectionStatus(Integer connectionStatus) {
        this.connectionStatus = connectionStatus;
    }

    public String getBrokerPublicAddress() {
        return brokerPublicAddress;
    }

    public void setBrokerPublicAddress(String brokerPublicAddress) {
        this.brokerPublicAddress = brokerPublicAddress;
    }

    public String getBrokerPrivateAddress() {
        return brokerPrivateAddress;
    }

    public void setBrokerPrivateAddress(String brokerPrivateAddress) {
        this.brokerPrivateAddress = brokerPrivateAddress;
    }

    public Date getDisconnectTime() {
        return disconnectTime;
    }

    public void setDisconnectTime(Date disconnectTime) {
        this.disconnectTime = disconnectTime;
    }
}
