package com.keepthinker.wavemessaging.nosql.redis;

import com.google.protobuf.InvalidProtocolBufferException;
import com.keepthinker.wavemessaging.nosql.MessageInfoNoSqlDao;
import com.keepthinker.wavemessaging.nosql.redis.model.MessageInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

import static com.keepthinker.wavemessaging.proto.WmpMessageProtos.WmpPublishMessageBody;

/**
 * Created by keepthinker on 2017/4/9.
 */

@Repository
public class MessageInfoRedisDaoImpl implements MessageInfoNoSqlDao {
    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    private WmShardRedisTemplate shardRedisTemplate;


    @Override
    public void save(MessageInfo messageInfo) {
        Map<String, String> map = new HashMap<>();
        map.put(RedisUtils.MESSAGE_ID, String.valueOf(messageInfo.getId()));
        map.put(RedisUtils.MESSAGE_CONTENT, messageInfo.getContent());
        map.put(RedisUtils.MESSAGE_CREATE_TIME, String.valueOf(messageInfo.getCreateTime().getTime()));
        map.put(RedisUtils.MESSAGE_TIMEOUT, String.valueOf(messageInfo.getTimeout()));
        String messageKey = RedisUtils.getMessageKey(messageInfo.getId());
        shardRedisTemplate.hmset(RedisUtils.getMessageKey(messageInfo.getId()), map);
        shardRedisTemplate.expire(messageKey, 24 * 3600);
    }

    @Override
    public WmpPublishMessageBody getPublishMessageBody(long messageId) {
       byte[] publishBody = shardRedisTemplate.hgetByte(RedisUtils.getMessageKey(messageId),
                RedisUtils.MESSAGE_PUBLISH_BODY);
        WmpPublishMessageBody body;
        try {
            body = WmpPublishMessageBody.parseFrom(publishBody);
        } catch (InvalidProtocolBufferException e) {
            LOGGER.error("error in parsing publish body in redis db", e);
            throw new RuntimeException(e);
        }
        return body;
    }

    @Override
    public long savePublishMessageBody(WmpPublishMessageBody newBody) {
        return shardRedisTemplate.hset(RedisUtils.getMessageKey(newBody.getMessageId()),
                RedisUtils.MESSAGE_PUBLISH_BODY,
                newBody.toByteArray());
    }

    @Override
    public long expire(String key, int seconds){
        return shardRedisTemplate.expire(key, seconds);
    }


}
