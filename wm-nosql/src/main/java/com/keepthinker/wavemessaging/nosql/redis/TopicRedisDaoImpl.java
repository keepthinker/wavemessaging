package com.keepthinker.wavemessaging.nosql.redis;

import com.keepthinker.wavemessaging.nosql.ScanHandler;
import com.keepthinker.wavemessaging.nosql.TopicNoSqlDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.ScanResult;
import redis.clients.jedis.ShardedJedis;

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

    @Override
    public void sscanHandle(String topicKey, ScanHandler scanHandler) {

        ShardedJedis jedis =  shardRedisTemplate.getShardedJedisPool().getResource();

        ScanResult<String> scanResult = new ScanResult<String>("0", null);
        do {
           scanResult = jedis.sscan(topicKey, scanResult.getStringCursor());
           scanHandler.handle(scanResult.getResult());
        }while(!"0".equals(scanResult.getStringCursor()));

        jedis.close();
    }


}
