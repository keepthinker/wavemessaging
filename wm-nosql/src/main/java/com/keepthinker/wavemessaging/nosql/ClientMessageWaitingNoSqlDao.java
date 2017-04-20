package com.keepthinker.wavemessaging.nosql;

/**
 * Created by keepthinker on 2017/4/18.
 */
public interface ClientMessageWaitingNoSqlDao {

    void enqueue(String clientId, long messageId);

    Long dequeue(String clientId);
}
