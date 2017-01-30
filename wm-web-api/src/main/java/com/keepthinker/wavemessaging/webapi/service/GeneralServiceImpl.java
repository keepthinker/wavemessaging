package com.keepthinker.wavemessaging.webapi.service;

import com.keepthinker.wavemessaging.core.utils.CryptoUtils;
import com.keepthinker.wavemessaging.core.utils.WmUtils;
import com.keepthinker.wavemessaging.dao.ClientInfoMapper;
import com.keepthinker.wavemessaging.dao.model.ClientInfo;
import com.keepthinker.wavemessaging.redis.RedisUtils;
import com.keepthinker.wavemessaging.redis.WmStringRedisTemplate;
import com.keepthinker.wavemessaging.webapi.model.RegisterInfo;
import com.keepthinker.wavemessaging.webapi.model.RegisterResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.ShardedJedisPipeline;

import java.util.Date;

@Service
public class GeneralServiceImpl implements GeneralService {
    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    private ClientInfoMapper clientInfoMapper;

    @Autowired
    private WmStringRedisTemplate redisTemplate;

    @Override
    public RegisterResult register(RegisterInfo registerInfo) {
        long clientId = WmUtils.generateUniqueId();
        ClientInfo clientInfo = new ClientInfo();
        clientInfo.setUsername(CryptoUtils.hash(registerInfo.getUsername()));
        clientInfo.setPassword(CryptoUtils.hash(registerInfo.getPassword()));
        clientInfo.setClientId(clientId);
        RegisterResult result = new RegisterResult();
        try {
            clientInfoMapper.insert(clientInfo);
            result.setClientId(clientInfo.getClientId());

            ShardedJedisPipeline pipeline = redisTemplate.getShardedJedisPool().getResource().pipelined();
            pipeline.hset(RedisUtils.getClientIdKey(clientId), RedisUtils.CI_ACCESS_TIME, Long.toString(new Date().getTime()));
            pipeline.hset(RedisUtils.getClientIdKey(clientId), RedisUtils.CI_USERNAME, clientInfo.getUsername());
            pipeline.hset(RedisUtils.getClientIdKey(clientId), RedisUtils.CI_PASSWORD, clientInfo.getPassword());

//			String username = RedisUtils.getUsernameKey(clientInfo.getUsername());
//			pipeline.hset(username, RedisUtils.UN_CLIENT_ID, Long.toString(clientId));
            pipeline.sync();
            result.setSuccess(true);
        } catch (Exception e) {
            //usually because of same record(username or clientId) in db
            LOGGER.warn("client insert exception", e);
            result.setSuccess(false);
        }
        return result;
    }

}
