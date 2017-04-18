package com.keepthinker.wavemessaging.nosql.redis;

import com.keepthinker.wavemessaging.nosql.MessageInfoNoSqlDao;
import com.keepthinker.wavemessaging.nosql.redis.RedisUtils;
import com.keepthinker.wavemessaging.nosql.redis.WmStringShardRedisTemplate;
import com.keepthinker.wavemessaging.nosql.redis.model.MessageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by keepthinker on 2017/4/9.
 */

@Repository
public class MessageInfoRedisDaoImpl implements MessageInfoNoSqlDao {

    @Autowired
    private WmStringShardRedisTemplate shardRedisTemplate;


    @Override
    public void save(MessageInfo messageInfo) {
        Map<String, String> map = new HashMap<>();
        map.put(RedisUtils.MESSAGE_ID, String.valueOf(messageInfo.getId()));
        map.put(RedisUtils.MESSAGE_CONTENT, messageInfo.getContent());
        map.put(RedisUtils.MESSAGE_CREATE_TIME, String.valueOf(messageInfo.getCreateTime().getTime()));
        map.put(RedisUtils.MESSAGE_TIMEOUT, String.valueOf(messageInfo.getTimeout()));
        shardRedisTemplate.hmset(RedisUtils.getMessageKey(messageInfo.getId()), map);
    }
}
