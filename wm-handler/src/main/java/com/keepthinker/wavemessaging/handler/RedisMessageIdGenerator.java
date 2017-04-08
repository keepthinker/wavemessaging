package com.keepthinker.wavemessaging.handler;

import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by keepthinker on 2017/4/4.
 */
@Service
public class RedisMessageIdGenerator implements MessageIdGenerator{

    @Resource(name = "msgIdGenRedisTemplate1")
    private StringRedisTemplate msgIdGen;

    @Override
    public long generate() {
        RedisConnection redisConnection = msgIdGen.getConnectionFactory().getConnection();
        long msgId = msgIdGen.getConnectionFactory().getConnection().incr("id.gen".getBytes());
        redisConnection.close();
        return msgId;
    }
}