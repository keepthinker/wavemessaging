import com.keepthinker.wavemessaging.redis.WmStringShardRedisTemplate;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

public class RedisTest {

    public static void main(String[] args) {
        new RedisTest().test();
    }

    @Test
    public void test() {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-redis.xml");
        ShardedJedisPool shardedJedisPool = context.getBean("shardedJedisPool", ShardedJedisPool.class);
        System.out.println("shard" + shardedJedisPool);

        for (int i = 0; i < 100; i++) {
            ShardedJedis jedis = shardedJedisPool.getResource();
            System.out.println(jedis.set("key" + i, "value" + i));
            System.out.println(jedis.get("key" + i));
            System.out.println(jedis.del("key" + i));
            jedis.close();
        }
        WmStringShardRedisTemplate wmtringRedisTemplate = context.getBean("wmStringRedisTemplate", WmStringShardRedisTemplate.class);

        for (int i = 0; i < 100; i++) {
            System.out.println(wmtringRedisTemplate.set("key" + i, "value" + i));
            System.out.println(wmtringRedisTemplate.get("key" + i));
            System.out.println(wmtringRedisTemplate.del("key" + i));
        }
    }
}
