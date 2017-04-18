package com.keepthinker.wavemessaging.nosql.redis;

import com.keepthinker.wavemessaging.nosql.ClientMessageSendingNoSqlDao;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by keepthinker on 2017/4/18.
 */
public class ClientMessageSendingRedisDaoImpl implements ClientMessageSendingNoSqlDao {
    @Autowired
    private WmStringShardRedisTemplate shardRedisTemplate;

    @Override
    public boolean setNotExist(String clientId, String messageId) {
        return shardRedisTemplate.setnx(RedisUtils.getClientMessageSendingKey(clientId), messageId);
    }

    @Override
    public String get(String clientId) {

        return shardRedisTemplate.get(RedisUtils.getClientMessageSendingKey(clientId));
    }

    @Override
    public Long delete(String clientId) {

        return shardRedisTemplate.del(RedisUtils.getClientMessageSendingKey(clientId));
    }

}
