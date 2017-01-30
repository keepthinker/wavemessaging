package com.keepthinker.wavemessaging.server.proto;

import com.keepthinker.wavemessaging.core.ClientType;
import com.keepthinker.wavemessaging.core.Constants;
import com.keepthinker.wavemessaging.core.ProtocolService;
import com.keepthinker.wavemessaging.core.utils.CryptoUtils;
import com.keepthinker.wavemessaging.core.utils.MqttUtils;
import com.keepthinker.wavemessaging.core.utils.WmUtils;
import com.keepthinker.wavemessaging.core.utils.ZkCommonUtils;
import com.keepthinker.wavemessaging.redis.RedisUtils;
import com.keepthinker.wavemessaging.redis.WmStringRedisTemplate;
import com.keepthinker.wavemessaging.server.HandlerChannelMananger;
import com.keepthinker.wavemessaging.server.ServerStartup;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.mqtt.MqttConnAckMessage;
import io.netty.handler.codec.mqtt.MqttConnectMessage;
import io.netty.handler.codec.mqtt.MqttConnectPayload;
import io.netty.handler.codec.mqtt.MqttConnectVariableHeader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Receiving connect request from clients including sdk client and intranet service.<br/>
 * Process Intranet service request directly.<br/>
 * Dispatch sdk connect request to handler.<br/>
 */
@Service
public class ConnectService implements ProtocolService<MqttConnectMessage> {

    private static final Logger LOGGER = LogManager.getLogger();

    @Resource
    private ServerStartup serverStartup;

    @Resource
    private HandlerChannelMananger handlerChannelManager;

    @Autowired
    private WmStringRedisTemplate redisTemplate;

    @Override
    public void handle(ChannelHandlerContext ctx, MqttConnectMessage msg) {
        LOGGER.info("messageType: " + msg.fixedHeader().messageType());
        MqttConnectVariableHeader variableHeader = msg.variableHeader();
        LOGGER.info("variableHeader: " + variableHeader);
        MqttConnectPayload payload = msg.payload();
        LOGGER.info("payload: " + payload);
        if (variableHeader.hasUserName() == false) {// other nodes(handler etc.) in intranet don't need authentication
            handleHandlerConnect(ctx, msg);
        } else {
            handlerSdkConnect(ctx, msg);
        }
    }

    private void handleHandlerConnect(ChannelHandlerContext ctx, MqttConnectMessage msg) {
        String clientId = msg.payload().clientIdentifier();
        if (clientId.split(":")[0].equals(Constants.NODE_NAME_HANDLER)) {
            handlerChannelManager.add(ctx.channel());
            ZkCommonUtils.increaseZkServerInfo(WmUtils.getIPV4Private(), serverStartup.getPort(), ClientType.HANDLER);
            ctx.writeAndFlush(MqttUtils.CONNACK_ACCEPTED_MESSAGE);
        } else {
            rejectId(ctx, clientId);
        }
    }

    private void rejectId(ChannelHandlerContext ctx, String clientId) {
        MqttConnAckMessage connAckMessage = MqttUtils.CONNECTION_REFUSED_IDENTIFIER_REJECTED_MESSAGE;
        ctx.writeAndFlush(connAckMessage);
        LOGGER.warn("rejected identifier: " + clientId);
    }

    private void handlerSdkConnect(ChannelHandlerContext ctx, MqttConnectMessage msg) {
        MqttConnectPayload payload = msg.payload();
        String clientId = payload.clientIdentifier();
        try {
            Integer.valueOf(clientId);
        } catch (Exception e) {
            rejectId(ctx, clientId);
            return;
        }

        MqttConnectVariableHeader header = msg.variableHeader();
        String usernameHash = CryptoUtils.hash(payload.userName());
        String passwordHash = CryptoUtils.hash(payload.password());
        if (header.hasUserName() && header.hasPassword()) {
            redisTemplate.get(RedisUtils.getClientIdKey(clientId));
            String unHash = CryptoUtils.hash(redisTemplate.hget(RedisUtils.getClientIdKey(clientId), RedisUtils.CI_USERNAME));
            String pwdHash = CryptoUtils.hash(redisTemplate.hget(RedisUtils.getClientIdKey(clientId), RedisUtils.CI_PASSWORD));
            if (usernameHash.equals(unHash) && passwordHash.equals(pwdHash)) {
                MqttConnAckMessage connAckMessage = MqttUtils.CONNACK_ACCEPTED_MESSAGE;
                ctx.writeAndFlush(connAckMessage);
            } else {
                rejectUsernameAndPassword(ctx, usernameHash, passwordHash, clientId);
            }
        } else {
            rejectUsernameAndPassword(ctx, usernameHash, passwordHash, clientId);
        }
    }

    private void rejectUsernameAndPassword(ChannelHandlerContext ctx, String usernameHash, String passwordHash, String clientId) {
        MqttConnAckMessage connAckMessage = MqttUtils.CONNACK_REFUSED_BAD_USER_NAME_OR_PASSWORD_MESSAGE;
        ctx.writeAndFlush(connAckMessage);
        LOGGER.warn("rejected username({}) or password({}) with identifier({})", usernameHash, passwordHash, clientId);

    }
}