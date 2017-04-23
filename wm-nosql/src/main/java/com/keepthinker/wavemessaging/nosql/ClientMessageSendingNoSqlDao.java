package com.keepthinker.wavemessaging.nosql;

/**
 * Created by keepthinker on 2017/4/18.
 */
public interface ClientMessageSendingNoSqlDao {

    boolean setNotExist(String clientId, long messageId);

    Long get(String clientId);

    Long delete(String clientId);
}
