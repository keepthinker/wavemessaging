package com.keepthinker.wavemessaging.nosql.redis;

import com.keepthinker.wavemessaging.nosql.ClientMessageWaitingNoSqlDao;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by keepthinker on 2017/4/18.
 */
public class ClientMessageWaitingRedisDaoImpl implements ClientMessageWaitingNoSqlDao {
    @Autowired
    private WmStringShardRedisTemplate shardRedisTemplate;

    @Override
    public void enqueue(String clientId, long messageId) {
        shardRedisTemplate.lpush(RedisUtils.getClientMessageWaitingKey(clientId), String.valueOf(messageId));
    }

    @Override
    public long dequeue(String clientId) {
        return Long.valueOf(shardRedisTemplate.rpop(RedisUtils.getClientMessageWaitingKey(clientId)));
    }
}
