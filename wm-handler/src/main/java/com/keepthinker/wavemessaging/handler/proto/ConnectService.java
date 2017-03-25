package com.keepthinker.wavemessaging.handler.proto;

import com.keepthinker.wavemessaging.core.ProtocolService;
import com.keepthinker.wavemessaging.core.utils.Constants;
import com.keepthinker.wavemessaging.handler.ChannelHolder;
import com.keepthinker.wavemessaging.handler.utils.HandlerUtils;
import com.keepthinker.wavemessaging.proto.WmpConnAckMessage;
import com.keepthinker.wavemessaging.proto.WmpConnectMessage;
import com.keepthinker.wavemessaging.proto.WmpMessageProtos;
import com.keepthinker.wavemessaging.redis.RedisUtils;
import com.keepthinker.wavemessaging.redis.WmStringRedisTemplate;
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
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private ChannelHolder holder;

    @Autowired
    private WmStringRedisTemplate redisTemplate;

    @Override
    public void handle(ChannelHandlerContext ctx, WmpConnectMessage msg) {

        WmpMessageProtos.WmpConnectMessageBody messageBody = msg.getBody();

        String clientId = messageBody.getClientId();

        redisTemplate.get(RedisUtils.getClientIdKey(clientId));
        String tokenRedis = redisTemplate.hget(RedisUtils.getClientIdKey(clientId), RedisUtils.CI_TOKEN);

        String responseClientId = Constants.CLIENT_ID_PREFIX_HANDLER + clientId;
        if (tokenRedis != null && tokenRedis.equals(messageBody.getToken())) {

            WmpConnAckMessage response = HandlerUtils.createSdkConnAckResultMessage(responseClientId,
                    WmpMessageProtos.WmpConnectReturnCode.ACCEPTED);
            ctx.writeAndFlush(response);

        } else {
            WmpConnAckMessage response = HandlerUtils.createSdkConnAckResultMessage(responseClientId,
                    WmpMessageProtos.WmpConnectReturnCode.REFUSED_NOT_AUTHORIZED);
            ctx.writeAndFlush(response);
            LOGGER.warn("rejected token({}) with identifier({})", messageBody.getToken(), clientId);
        }
    }
}
