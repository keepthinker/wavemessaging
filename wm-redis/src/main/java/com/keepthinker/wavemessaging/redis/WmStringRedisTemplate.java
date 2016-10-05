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

	public String set(String key, String value){
		ShardedJedis jedis = shardedJedisPool.getResource();
		String result = jedis.set(key, value);
		jedis.close();
		return result;
	}
	
	public String get(String key){
		ShardedJedis jedis = shardedJedisPool.getResource();
		String value = jedis.get(key);
		jedis.close();
		return value;
	}
	
	public Long del(String key){
		ShardedJedis jedis = shardedJedisPool.getResource();
		Long value = jedis.del(key);
		jedis.close();
		return value;
		
	}
}
