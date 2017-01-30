package com.keepthinker.wavemessaging.client.dao;

/**
 * Created by keepthinker on 2017/1/27.
 */
public interface ClientInfoDao {
    /**
     * at most one record
     *
     * @param clientInfo
     */
    void insert(ClientInfo clientInfo);

    ClientInfo get();
}
