package com.keepthinker.wavemessaging.nosql.redis;

import com.keepthinker.wavemessaging.nosql.ClientInfoNoSqlDao;
import com.keepthinker.wavemessaging.nosql.redis.model.ClientInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.ShardedJedisPipeline;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by keepthinker on 2017/4/9.
 */
@Repository
public class ClientInfoRedisDaoImpl implements ClientInfoNoSqlDao {

    @Autowired
    private WmShardRedisTemplate shardRedisTemplate;

    @Override
    public void save(ClientInfo clientInfo) {
        ShardedJedisPipeline pipeline = shardRedisTemplate.getShardedJedisPool().getResource().pipelined();

        Map<String, String> map = new HashMap<>();
        if(StringUtils.isNotBlank(clientInfo.getUsername())) {
            map.put(RedisUtils.CLIENT_USERNAME, clientInfo.getUsername());
            String username = RedisUtils.getUsernameKey(clientInfo.getUsername());
            pipeline.hset(username, RedisUtils.UN_CLIENT_ID, Long.toString(clientInfo.getClientId()));
        }
        if(StringUtils.isNotBlank(clientInfo.getPassword())) {
            map.put(RedisUtils.CLIENT_PASSWORD, clientInfo.getPassword());
        }
        if(clientInfo.getConnectionStatus() != null) {
            map.put(RedisUtils.CLIENT_CONNECTION_STATUS, String.valueOf(clientInfo.getConnectionStatus()));
        }
        if(StringUtils.isNotBlank(clientInfo.getBrokerPrivateAddress())) {
            map.put(RedisUtils.CLIENT_BROKER_PRIVATE_ADDRESS, clientInfo.getBrokerPrivateAddress());
        }
        if(StringUtils.isNotBlank(clientInfo.getBrokerPublicAddress())) {
            map.put(RedisUtils.CLIENT_BROKER_PUBLIC_ADDRESS, clientInfo.getBrokerPublicAddress());
        }
        if(clientInfo.getDisconnectTime() != null){
            map.put(RedisUtils.CLIENT_DISCONNECT_TIME, Long.toString(clientInfo.getDisconnectTime().getTime()));
        }

        map.put(RedisUtils.CLIENT_ACCESS_TIME, Long.toString(new Date().getTime()));
        pipeline.hmset(RedisUtils.getClientKey(clientInfo.getClientId()), map);

        pipeline.sync();
    }

    public String getClientId(String usernameHash){
        return shardRedisTemplate.hget(RedisUtils.getUsernameKey(usernameHash), RedisUtils.UN_CLIENT_ID);
    }

    public void setToken(String clientId, String token){
        shardRedisTemplate.hset(RedisUtils.getClientKey(clientId), RedisUtils.CLIENT_TOKEN, token);
    }

    public String getToken(String clientId){
        return shardRedisTemplate.hget(RedisUtils.getClientKey(clientId), RedisUtils.CLIENT_TOKEN);
    }

    @Override
    public String getBrokerPrivateAddress(String clientId){
        return shardRedisTemplate.hget(RedisUtils.getClientKey(clientId), RedisUtils.CLIENT_BROKER_PRIVATE_ADDRESS);
    }

    @Override
    public int getConnectionStatus(String clientId){
        return Integer.valueOf(shardRedisTemplate.hget(RedisUtils.getClientKey(clientId), RedisUtils.CLIENT_CONNECTION_STATUS));
    }



}
