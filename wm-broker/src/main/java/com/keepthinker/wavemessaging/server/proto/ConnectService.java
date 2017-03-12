package com.keepthinker.wavemessaging.server.proto;

import com.keepthinker.wavemessaging.core.ClientType;
import com.keepthinker.wavemessaging.core.utils.Constants;
import com.keepthinker.wavemessaging.core.ProtocolService;
import com.keepthinker.wavemessaging.core.utils.*;
import com.keepthinker.wavemessaging.redis.WmStringRedisTemplate;
import com.keepthinker.wavemessaging.server.HandlerChannelMananger;
import com.keepthinker.wavemessaging.server.SDKChannelManager;
import com.keepthinker.wavemessaging.server.ServerStartup;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.mqtt.MqttConnAckMessage;
import io.netty.handler.codec.mqtt.MqttConnectMessage;
import io.netty.handler.codec.mqtt.MqttConnectPayload;
import io.netty.handler.codec.mqtt.MqttConnectVariableHeader;
import org.apache.commons.lang.StringUtils;
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

    @Resource
    private SDKChannelManager sdkChannelManager;

    @Autowired
    private WmStringRedisTemplate redisTemplate;

    @Override
    public void handle(ChannelHandlerContext ctx, MqttConnectMessage msg) {
        LOGGER.info("messageType: " + msg.fixedHeader().messageType());
        MqttConnectVariableHeader variableHeader = msg.variableHeader();
        LOGGER.info("variableHeader: " + variableHeader);
        MqttConnectPayload payload = msg.payload();
        LOGGER.info("payload: " + payload);
        String clientIdStr = msg.payload().clientIdentifier();
        try {
            if (clientIdStr.startsWith(Constants.CLIENT_ID_PREFIX_HANDLER)) {
                handleHandlerConnect(ctx, msg);
            } else {
                if(StringUtils.isNumeric(clientIdStr)){
                    handlerSdkConnect(ctx, msg);
                }else{
                    LOGGER.warn("Client() is not valid.", clientIdStr);
                }
            }
        }catch(Exception e){
            LOGGER.error("error in handling connect action|{}", e);
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
        long clientIdLong;
        try {
            clientIdLong = Long.parseLong(clientId);
        } catch (Exception e) {
            rejectId(ctx, clientId);
            return;
        }
        sdkChannelManager.put(clientIdLong, ctx.channel());
        handlerChannelManager.get(clientId).writeAndFlush(msg);

    }
}