package com.keepthinker.wavemessaging.nosql.redis;

import com.keepthinker.wavemessaging.nosql.ClientMessageSendingNoSqlDao;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Created by keepthinker on 2017/4/18.
 */
@Repository
public class ClientMessageSendingRedisDaoImpl implements ClientMessageSendingNoSqlDao {
    @Autowired
    private WmShardRedisTemplate shardRedisTemplate;

    @Override
    public boolean setNotExist(String clientId, long messageId) {
        return shardRedisTemplate.setnx(RedisUtils.getClientMessageSendingKey(clientId), String.valueOf(messageId));
    }

    @Override
    public Long get(String clientId) {
        String messageId = shardRedisTemplate.get(RedisUtils.getClientMessageSendingKey(clientId));
        if(StringUtils.isNotBlank(messageId)){
            return Long.valueOf(messageId);
        }else{
            return null;
        }
    }

    @Override
    public Long delete(String clientId) {

        return shardRedisTemplate.del(RedisUtils.getClientMessageSendingKey(clientId));
    }

}
