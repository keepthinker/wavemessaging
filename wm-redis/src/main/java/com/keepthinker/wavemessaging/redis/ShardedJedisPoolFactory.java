package com.keepthinker.wavemessaging.redis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.FactoryBean;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

import java.util.ArrayList;

/**
 * host:port;host:port
 *
 * @author keepthinker
 */
public class ShardedJedisPoolFactory implements FactoryBean<ShardedJedisPool> {
    private static final Logger log = LogManager.getLogger();

    private GenericObjectPoolConfig poolConfig;

    private String hostPorts;

    @Override
    public ShardedJedisPool getObject() throws Exception {
        log.info(hostPorts);

        ArrayList<JedisShardInfo> infos = new ArrayList<>();

        int pos = 0;
        String hostTemp = null;
        for (int i = 0; i <= hostPorts.length(); i++) {
            if (i == hostPorts.length() || hostPorts.charAt(i) == ';') {
                int port = Integer.valueOf(hostPorts.substring(pos, i));
                infos.add(new JedisShardInfo(hostTemp, port));
                pos = i + 1;
            } else if (hostPorts.charAt(i) == ':') {
                hostTemp = hostPorts.substring(pos, i);
                pos = i + 1;
            }
        }

        log.info(infos);

        ShardedJedisPool shardedJedisPool = new ShardedJedisPool(poolConfig, infos);
        return shardedJedisPool;
    }

    @Override
    public Class<?> getObjectType() {
        return ShardedJedisPoolFactory.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public void setPoolConfig(GenericObjectPoolConfig poolConfig) {
        this.poolConfig = poolConfig;
    }

    public void setHostPorts(String hostPorts) {
        this.hostPorts = hostPorts;
    }

}
