import com.keepthinker.wavemessaging.nosql.redis.WmStringShardRedisTemplate;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

public class RedisTest {
    private static ApplicationContext context = new ClassPathXmlApplicationContext("spring-redis.xml");

    public static void main(String[] args) {
        new RedisTest().test();
    }

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
        WmStringShardRedisTemplate wmtringRedisTemplate = context.getBean("wmStringRedisTemplate", WmStringShardRedisTemplate.class);

        for (int i = 0; i < 100; i++) {
            System.out.println(wmtringRedisTemplate.set("key" + i, "value" + i));
            System.out.println(wmtringRedisTemplate.get("key" + i));
            System.out.println(wmtringRedisTemplate.del("key" + i));
        }
    }

    @Test
    public void testListPop() {
        ShardedJedisPool shardedJedisPool = context.getBean("shardedJedisPool", ShardedJedisPool.class);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int count = 10;
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < count; i++){
                    ShardedJedis jedis = shardedJedisPool.getResource();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("push " + String.valueOf(i));
                    jedis.lpush("list", String.valueOf(i));
                    jedis.close();
                }
            }
        }).start();


        for(int i = 0; i < count; i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ShardedJedis jedis = shardedJedisPool.getResource();
                    System.out.println(jedis.brpop(10, "list"));
                    jedis.close();
                }
            }).start();
        }

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
