package com.keepthinker.wavemessaging.nosql;

/**
 * Created by keepthinker on 2017/5/6.
 */
public interface TopicNoSqlDao {

    Long save(String topicKey, String clientId);

}
