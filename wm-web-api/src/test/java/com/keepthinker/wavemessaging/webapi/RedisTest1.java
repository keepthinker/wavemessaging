package com.keepthinker.wavemessaging.webapi;

import com.keepthinker.wavemessaging.redis.WmStringRedisTemplate;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

public class RedisTest1 {
    private ApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");

    @Test
    public void test() {
        ShardedJedisPool shardedJedisPool = context.getBean("shardedJedisPool", ShardedJedisPool.class);
        System.out.println("shard" + shardedJedisPool);

        for (int i = 0; i < 100; i++) {
            ShardedJedis jedis = shardedJedisPool.getResource();
            System.out.println(jedis.set("key" + i, "value" + i));
            System.out.println(jedis.get("key" + i));
            System.out.println(jedis.del("key" + i));
            jedis.close();
        }
        WmStringRedisTemplate wmtringRedisTemplate = context.getBean("wmStringRedisTemplate", WmStringRedisTemplate.class);

        for (int i = 0; i < 100; i++) {
            System.out.println(wmtringRedisTemplate.set("key" + i, "value" + i));
            System.out.println(wmtringRedisTemplate.get("key" + i));
            System.out.println(wmtringRedisTemplate.del("key" + i));
        }
    }

}
