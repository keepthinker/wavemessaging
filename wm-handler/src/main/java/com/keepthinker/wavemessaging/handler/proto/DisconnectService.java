package com.keepthinker.wavemessaging.handler.proto;

import com.keepthinker.wavemessaging.core.ProtocolService;
import com.keepthinker.wavemessaging.core.utils.Constants;
import com.keepthinker.wavemessaging.proto.WmpDisconnectMessage;
import com.keepthinker.wavemessaging.nosql.ClientInfoNoSqlDao;
import com.keepthinker.wavemessaging.nosql.redis.model.ClientInfo;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by keepthinker on 2017/4/4.
 */
@Service
public class DisconnectService implements ProtocolService<WmpDisconnectMessage> {

    private static final Logger LOGGER = LogManager.getLogger();


    @Autowired
    private ClientInfoNoSqlDao clientInfoCacheDao;

    @Override
    public void handle(ChannelHandlerContext ctx, WmpDisconnectMessage msg) {

        ClientInfo clientInfo = new ClientInfo();
        clientInfo.setClientId(Long.valueOf(msg.getBody().getClientId()));
        clientInfo.setDisconnectTime(new Date());
        clientInfo.setConnectionStatus(Constants.CONNECTION_STATUTS_DISCONNECTED);
        clientInfoCacheDao.save(clientInfo);

    }
}
