package com.keepthinker.wavemessaging.handler.proto;

import com.keepthinker.wavemessaging.core.ProtocolService;
import com.keepthinker.wavemessaging.proto.WmpDisConnectMessage;
import com.keepthinker.wavemessaging.redis.RedisUtils;
import com.keepthinker.wavemessaging.redis.WmStringShardRedisTemplate;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPipeline;

import java.util.Date;

/**
 * Created by keepthinker on 2017/4/4.
 */
@Service
public class DisConnectService implements ProtocolService<WmpDisConnectMessage> {

    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    private WmStringShardRedisTemplate redisTemplate;

    @Override
    public void handle(ChannelHandlerContext ctx, WmpDisConnectMessage msg) {
        ShardedJedis jedis = redisTemplate.getShardedJedisPool().getResource();
        ShardedJedisPipeline pipeline = jedis.pipelined();
        pipeline.hset(RedisUtils.getClientKey(msg.getBody().getClientId()), RedisUtils.CLIENT_CONNECTION_STATUS, "0");
        pipeline.hset(RedisUtils.getClientKey(msg.getBody().getClientId()), RedisUtils.CLIENT_DISCONNECT_TIME, String.valueOf(new Date().getTime()));
        pipeline.sync();
    }
}
