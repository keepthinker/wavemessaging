package com.keepthinker.wavemessaging.redis;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

public class WmStringRedisTemplate {
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

    public boolean exists(String key){
        ShardedJedis jedis = shardedJedisPool.getResource();
        boolean result = jedis.exists(key);
        return result;
    }

    /**
     * @param num the number to be added
     * @return size after increase
     */
    public Long hincr(String key, String field, int num){
        ShardedJedis jedis = shardedJedisPool.getResource();
        Long result = jedis.hincrBy(key, field, num);
        jedis.close();
        return result;
    }
}
