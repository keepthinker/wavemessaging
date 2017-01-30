package com.keepthinker.wavemessaging.client.dao;

import org.springframework.stereotype.Repository;

/**
 * mock a dao which access database
 * Created by keepthinker on 2017/1/28.
 */
@Repository
public class ClientInfoDaoImpl implements ClientInfoDao {

    private ClientInfo tempClientInfo;

    @Override
    public void insert(ClientInfo clientInfo) {
        tempClientInfo = clientInfo;
    }

    @Override
    public ClientInfo get() {
        return tempClientInfo;
    }
}
