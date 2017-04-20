package com.keepthinker.wavemessaging.server.proto;

import com.keepthinker.wavemessaging.core.ProtocolService;
import com.keepthinker.wavemessaging.core.utils.Constants;
import com.keepthinker.wavemessaging.core.utils.WmUtils;
import com.keepthinker.wavemessaging.core.utils.WmpUtils;
import com.keepthinker.wavemessaging.proto.WmpConnAckMessage;
import com.keepthinker.wavemessaging.proto.WmpConnectMessage;
import com.keepthinker.wavemessaging.proto.WmpMessageProtos;
import com.keepthinker.wavemessaging.nosql.redis.WmShardRedisTemplate;
import com.keepthinker.wavemessaging.server.HandlerChannelMananger;
import com.keepthinker.wavemessaging.server.SdkChannelManager;
import com.keepthinker.wavemessaging.server.StatisticsService;
import io.netty.channel.ChannelHandlerContext;
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
public class ConnectService implements ProtocolService<WmpConnectMessage> {

    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    private StatisticsService statisticsService;

    @Resource
    private HandlerChannelMananger handlerChannelManager;

    @Resource
    private SdkChannelManager sdkChannelManager;

    @Resource
    private WmShardRedisTemplate redisTemplate;

    @Override
    public void handle(ChannelHandlerContext ctx, WmpConnectMessage msg) {
        WmpMessageProtos.WmpConnectMessageBody payload = msg.getBody();
        LOGGER.info("payload: " + payload);
        String clientIdStr = payload.getClientId();
        try {
            if (clientIdStr.startsWith(Constants.CLIENT_ID_PREFIX_HANDLER)) {
                handleHandlerConnect(ctx, msg);
            } else {
                if(StringUtils.isNumeric(clientIdStr)){
                    handleSdkConnect(ctx, msg);
                }else{
                    LOGGER.warn("Client() is not valid.", clientIdStr);
                }
            }
        }catch(Exception e){
            LOGGER.error("error in handling connect action|{}", e);
        }
    }

    private void handleHandlerConnect(ChannelHandlerContext ctx, WmpConnectMessage msg) {
        String clientId = msg.getBody().getClientId();
        if (clientId.indexOf(Constants.CLIENT_ID_PREFIX_HANDLER) == 0) {
            handlerChannelManager.add(ctx.channel());
            ctx.writeAndFlush(WmpUtils.CONNACK_ACCEPTED_MESSAGE);
            statisticsService.countConnection();
        } else {
            rejectId(ctx, clientId);
        }
    }

    private void rejectId(ChannelHandlerContext ctx, String clientId) {
        WmpConnAckMessage connAckMessage = WmpUtils.CONNECTION_REFUSED_CLIENT_ID_REJECTED_MESSAGE;
        ctx.writeAndFlush(connAckMessage);
        LOGGER.warn("rejected identifier: " + clientId);
    }

    private void handleSdkConnect(ChannelHandlerContext ctx, WmpConnectMessage msg) {
        String clientId = msg.getBody().getClientId();
        try {
            Long.parseLong(clientId);
        } catch (Exception e) {
            rejectId(ctx, clientId);
            return;
        }
        sdkChannelManager.putChannel(clientId, ctx.channel());
        msg.setBody(msg.getBody().toBuilder().setBrokerAddress(WmUtils.getChannelLocalAddress(ctx.channel())).build());;
        handlerChannelManager.get(clientId).writeAndFlush(msg);

    }
}