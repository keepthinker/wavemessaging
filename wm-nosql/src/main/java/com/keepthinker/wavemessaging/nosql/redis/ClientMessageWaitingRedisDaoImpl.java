package com.keepthinker.wavemessaging.nosql.redis;

import com.keepthinker.wavemessaging.nosql.ClientMessageWaitingNoSqlDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Created by keepthinker on 2017/4/18.
 */
@Repository
public class ClientMessageWaitingRedisDaoImpl implements ClientMessageWaitingNoSqlDao {
    @Autowired
    private WmShardRedisTemplate shardRedisTemplate;

    @Override
    public void enqueue(String clientId, long messageId) {
        shardRedisTemplate.lpush(RedisUtils.getClientMessageWaitingKey(clientId), String.valueOf(messageId));
    }

    @Override
    public Long dequeue(String clientId) {
        String messageId = shardRedisTemplate.rpop(RedisUtils.getClientMessageWaitingKey(clientId));
        if(messageId == null){
            return null;
        }else {
            return Long.valueOf(messageId);
        }
    }
}
