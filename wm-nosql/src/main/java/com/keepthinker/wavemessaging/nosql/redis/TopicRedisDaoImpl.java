package com.keepthinker.wavemessaging.nosql.redis;

import com.keepthinker.wavemessaging.nosql.TopicNoSqlDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Created by keepthinker on 2017/5/6.
 */
@Repository
public class TopicRedisDaoImpl implements TopicNoSqlDao{

    @Autowired
    private WmShardRedisTemplate shardRedisTemplate;

    @Override
    public Long save(String topicKey, String... clientId) {
        return shardRedisTemplate.sadd(topicKey, clientId);
    }

    @Override
    public Long delete(String topicKey, String... clientId) {
        return shardRedisTemplate.srem(topicKey, clientId);
    }


}
