package com.keepthinker.wavemessaging.handler.proto;

import com.keepthinker.wavemessaging.core.utils.Constants;
import com.keepthinker.wavemessaging.core.ProtocolService;
import com.keepthinker.wavemessaging.core.model.ClientWillMessage;
import com.keepthinker.wavemessaging.core.utils.JsonUtils;
import com.keepthinker.wavemessaging.handler.ChannelHolder;
import com.keepthinker.wavemessaging.handler.utils.HandlerUtils;
import com.keepthinker.wavemessaging.redis.RedisUtils;
import com.keepthinker.wavemessaging.redis.WmStringRedisTemplate;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.mqtt.MqttConnectMessage;
import io.netty.handler.codec.mqtt.MqttConnectPayload;
import io.netty.handler.codec.mqtt.MqttConnectReturnCode;
import io.netty.handler.codec.mqtt.MqttSubscribePayload;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConnectService implements ProtocolService<MqttConnectMessage> {
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private ChannelHolder holder;

    @Autowired
    private WmStringRedisTemplate redisTemplate;

    @Override
    public void handle(ChannelHandlerContext ctx, MqttConnectMessage msg) {

        MqttConnectPayload payload = msg.payload();

        String clientId = payload.clientIdentifier();

        ClientWillMessage willMessage = JsonUtils.stringToObject(payload.willMessage(), ClientWillMessage.class);

        redisTemplate.get(RedisUtils.getClientIdKey(clientId));
        String tokenRedis = redisTemplate.hget(RedisUtils.getClientIdKey(clientId), RedisUtils.CI_TOKEN);

        String responseClientId = Constants.CLIENT_ID_PREFIX_HANDLER + clientId;
        if (tokenRedis != null && tokenRedis.equals(willMessage.getToken())) {

            MqttConnectMessage response = HandlerUtils.createSdkConnectResultMessage(responseClientId,
                    String.valueOf(MqttConnectReturnCode.CONNECTION_ACCEPTED.byteValue()));
            ctx.writeAndFlush(response);
        } else {
            MqttConnectMessage response = HandlerUtils.createSdkConnectResultMessage(responseClientId,
                    String.valueOf(MqttConnectReturnCode.CONNECTION_ACCEPTED.byteValue()));
            ctx.writeAndFlush(response);
            LOGGER.warn("rejected token({}) with identifier({})", willMessage.getToken(), clientId);
        }
    }
}
