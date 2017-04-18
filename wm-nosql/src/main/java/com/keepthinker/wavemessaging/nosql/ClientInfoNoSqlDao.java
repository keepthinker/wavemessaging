package com.keepthinker.wavemessaging.nosql;

import com.keepthinker.wavemessaging.nosql.redis.model.ClientInfo;

/**
 * Created by keepthinker on 2017/4/9.
 */
public interface ClientInfoNoSqlDao {
    void save(ClientInfo clientInfo);

    String getClientId(String usernameHash);

    void setToken(String clientId, String token);

    String getToken(String clientId);

    String getBrokerPrivateAddress(String clientId);

    int getConnectionStatus(String clientId);
}
