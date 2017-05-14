package com.keepthinker.wavemessaging.webapi.service;

import com.keepthinker.wavemessaging.core.utils.CryptoUtils;
import com.keepthinker.wavemessaging.core.utils.WmUtils;
import com.keepthinker.wavemessaging.dao.ClientInfoMapper;
import com.keepthinker.wavemessaging.dao.model.ClientInfo;
import com.keepthinker.wavemessaging.nosql.ClientInfoNoSqlDao;
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

import java.util.UUID;

@Service
public class GeneralServiceImpl implements GeneralService {
    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    private ClientInfoMapper clientInfoMapper;

    @Autowired
    private ClientInfoNoSqlDao clientInfoNoSqlDao;

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
        com.keepthinker.wavemessaging.nosql.redis.model.ClientInfo clientInfoRedis = new com.keepthinker.wavemessaging.nosql.redis.model.ClientInfo();
        clientInfoRedis.setClientId(clientInfo.getClientId());
        clientInfoRedis.setUsername(clientInfo.getUsername());
        clientInfoRedis.setPassword(clientInfo.getPassword());
        clientInfoNoSqlDao.save(clientInfoRedis);
    }

    @Override
    public LoginResult login(LoginInfo loginInfo) {
        String token = UUID.randomUUID().toString();
        String clientId = clientInfoNoSqlDao.getClientId(CryptoUtils.hash(loginInfo.getUsername()));
        if(StringUtils.isBlank(clientId)) {
            ClientInfo clientInfo = clientInfoMapper.selectByUsername(CryptoUtils.hash(loginInfo.getUsername()));
            saveClientInfoToRedis(clientInfo);
            clientId = String.valueOf(clientInfo.getClientId());
        }
        clientInfoNoSqlDao.setToken(clientId, token);
        LoginResult result = new LoginResult(clientId, token);
        return result;
    }


}
