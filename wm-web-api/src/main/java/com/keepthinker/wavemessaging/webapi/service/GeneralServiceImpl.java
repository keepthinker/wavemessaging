package com.keepthinker.wavemessaging.webapi.service;

import com.keepthinker.wavemessaging.core.utils.CryptoUtils;
import com.keepthinker.wavemessaging.core.utils.WmUtils;
import com.keepthinker.wavemessaging.dao.ClientInfoMapper;
import com.keepthinker.wavemessaging.dao.model.ClientInfo;
import com.keepthinker.wavemessaging.redis.RedisUtils;
import com.keepthinker.wavemessaging.redis.WmStringShardRedisTemplate;
import com.keepthinker.wavemessaging.webapi.model.LoginInfo;
import com.keepthinker.wavemessaging.webapi.model.LoginResult;
import com.keepthinker.wavemessaging.webapi.model.RegisterInfo;
import com.keepthinker.wavemessaging.webapi.model.RegisterResult;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.ShardedJedisPipeline;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class GeneralServiceImpl implements GeneralService {
    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    private ClientInfoMapper clientInfoMapper;

    @Autowired
    private WmStringShardRedisTemplate redisTemplate;

    @Transactional
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

            saveClientInfoToRedis(clientInfo);

            result.setSuccess(true);
        } catch (Exception e) {
            //usually because of same record(username or clientId) in db
            LOGGER.warn("client insert exception", e);
            result.setSuccess(false);
        }
        return result;
    }

    private void saveClientInfoToRedis(ClientInfo clientInfo){
        ShardedJedisPipeline pipeline = redisTemplate.getShardedJedisPool().getResource().pipelined();

        Map<String, String> map = new HashMap<>();
        map.put(RedisUtils.CLIENT_ACCESS_TIME, Long.toString(new Date().getTime()));
        map.put(RedisUtils.CLIENT_USERNAME, clientInfo.getUsername());
        map.put(RedisUtils.CLIENT_PASSWORD, clientInfo.getPassword());
        pipeline.hmset(RedisUtils.getClientKey(clientInfo.getClientId()), map);

        String username = RedisUtils.getUsernameKey(clientInfo.getUsername());
        pipeline.hset(username, RedisUtils.UN_CLIENT_ID, Long.toString(clientInfo.getClientId()));

        pipeline.sync();
    }

    @Override
    public LoginResult login(LoginInfo loginInfo) {
        String token = UUID.randomUUID().toString();
        String clientId = redisTemplate.hget(RedisUtils.getUsernameKey(CryptoUtils.hash(loginInfo.getUsername())), RedisUtils.UN_CLIENT_ID);
        if(StringUtils.isBlank(clientId)) {
            ClientInfo clientInfo = clientInfoMapper.selectByUsername(CryptoUtils.hash(loginInfo.getUsername()));
            saveClientInfoToRedis(clientInfo);
            clientId = String.valueOf(clientInfo.getClientId());
        }
        redisTemplate.hset(RedisUtils.getClientKey(clientId), RedisUtils.CLIENT_TOKEN, token);
        LoginResult result = new LoginResult(clientId, token);
        return result;
    }


}
