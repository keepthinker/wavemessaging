package com.keepthinker.wavemessaging.nosql.redis;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.util.Map;

public class WmStringShardRedisTemplate {
    private ShardedJedisPool shardedJedisPool;

    public ShardedJedisPool getShardedJedisPool() {
        return shardedJedisPool;
    }

    public void setShardedJedisPool(ShardedJedisPool shardedJedisPool) {
        this.shardedJedisPool = shardedJedisPool;
    }

    public String set(String key, String value) {
        ShardedJedis jedis = shardedJedisPool.getResource();
        String result = jedis.set(key, value);
        jedis.close();
        return result;
    }

    public boolean setnx(String key, String value) {
        ShardedJedis jedis = shardedJedisPool.getResource();
        Long result = jedis.setnx(key, value);
        jedis.close();
        return result == 1;
    }

    public String get(String key) {
        ShardedJedis jedis = shardedJedisPool.getResource();
        String value = jedis.get(key);
        jedis.close();
        return value;
    }

    public Long del(String key) {
        ShardedJedis jedis = shardedJedisPool.getResource();
        Long value = jedis.del(key);
        jedis.close();
        return value;
    }

    public Long hset(String key, String field, String value) {
        ShardedJedis jedis = shardedJedisPool.getResource();
        Long reply = jedis.hset(key, field, value);
        jedis.close();
        return reply;
    }

    public String hget(String key, String field) {
        ShardedJedis jedis = shardedJedisPool.getResource();
        String result = jedis.hget(key, field);
        jedis.close();
        return result;
    }

    public String hmset(String key, Map<String, String> map) {
        ShardedJedis jedis = shardedJedisPool.getResource();
        String result = jedis.hmset(key, map);
        jedis.close();
        return result;
    }


    public boolean exists(String key) {
        ShardedJedis jedis = shardedJedisPool.getResource();
        boolean result = jedis.exists(key);
        return result;
    }

    /**
     * @param num the number to be added
     * @return size after increase
     */
    public Long hincr(String key, String field, int num) {
        ShardedJedis jedis = shardedJedisPool.getResource();
        Long result = jedis.hincrBy(key, field, num);
        jedis.close();
        return result;
    }

    public Long lpush(String key, String value) {
        ShardedJedis jedis = shardedJedisPool.getResource();
        Long result = jedis.lpush(key, value);
        jedis.close();
        return result;
    }

    public String rpop(String key) {
        ShardedJedis jedis = shardedJedisPool.getResource();
        String result = jedis.lpop(key);
        jedis.close();
        return result;
    }

}