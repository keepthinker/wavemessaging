package com.keepthinker.wavemessaging.handler.proto;

import com.keepthinker.wavemessaging.core.ProtocolService;
import com.keepthinker.wavemessaging.core.utils.Constants;
import com.keepthinker.wavemessaging.core.utils.WmUtils;
import com.keepthinker.wavemessaging.core.utils.WmpActionLogger;
import com.keepthinker.wavemessaging.handler.utils.HandlerUtils;
import com.keepthinker.wavemessaging.proto.WmpConnAckMessage;
import com.keepthinker.wavemessaging.proto.WmpConnectMessage;
import com.keepthinker.wavemessaging.proto.WmpMessageProtos;
import com.keepthinker.wavemessaging.nosql.ClientInfoNoSqlDao;
import com.keepthinker.wavemessaging.nosql.redis.model.ClientInfo;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * handler connect request from sdk via broker
 */
@Service
public class ConnectService implements ProtocolService<WmpConnectMessage> {
    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    private ClientInfoNoSqlDao clientInfoCacheDao;

    @Override
    public void handle(ChannelHandlerContext ctx, WmpConnectMessage msg) {

        WmpMessageProtos.WmpConnectMessageBody messageBody = msg.getBody();

        String clientId = messageBody.getClientId();

        try {
            String tokenRedis = clientInfoCacheDao.getToken(clientId);
            if (tokenRedis != null && tokenRedis.equals(messageBody.getToken())) {
                WmpConnAckMessage response = HandlerUtils.createSdkConnAckResultMessage(clientId,
                        WmpMessageProtos.WmpConnectReturnCode.ACCEPTED);
                ctx.writeAndFlush(response);

                ClientInfo clientInfo = new ClientInfo();
                clientInfo.setClientId(Long.valueOf(messageBody.getClientId()));
                clientInfo.setConnectionStatus(Constants.CONNECTION_STATUTS_ONLINE);
                clientInfo.setBrokerPrivateAddress(WmUtils.getChannelRemoteAddress(ctx.channel()));
                clientInfo.setBrokerPublicAddress(messageBody.getBrokerAddress());
                clientInfoCacheDao.save(clientInfo);

                WmpActionLogger.connect(msg.getBody().getClientId(), msg.getVersion());
            } else {
                WmpConnAckMessage response = HandlerUtils.createSdkConnAckResultMessage(clientId,
                        WmpMessageProtos.WmpConnectReturnCode.REFUSED_NOT_AUTHORIZED);
                ctx.writeAndFlush(response);
                LOGGER.warn("rejected token({}) with identifier({})", messageBody.getToken(), clientId);
            }
        }catch(Exception e){
            LOGGER.error("unexpected error|{}|{}", clientId, e);
            WmpConnAckMessage response = HandlerUtils.createSdkConnAckResultMessage(clientId,
                    WmpMessageProtos.WmpConnectReturnCode.REFUSED_NOT_AUTHORIZED);
            ctx.writeAndFlush(response);
        }
    }
}
