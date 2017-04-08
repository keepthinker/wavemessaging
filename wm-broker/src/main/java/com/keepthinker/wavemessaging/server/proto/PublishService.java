package com.keepthinker.wavemessaging.server.proto;

import com.keepthinker.wavemessaging.core.ProtocolService;
import com.keepthinker.wavemessaging.core.utils.WmUtils;
import com.keepthinker.wavemessaging.proto.WmpMessageProtos;
import com.keepthinker.wavemessaging.proto.WmpPublishMessage;
import com.keepthinker.wavemessaging.server.HandlerChannelMananger;
import com.keepthinker.wavemessaging.server.SdkChannelManager;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by keepthinker on 2017/4/2.
 */
@Service
public class PublishService implements ProtocolService<WmpPublishMessage> {

    private static final Logger LOGGER = LogManager.getLogger();

    @Resource
    private HandlerChannelMananger handlerChannelManager;

    @Resource
    private SdkChannelManager sdkChannelManager;

    @Override
    public void handle(ChannelHandlerContext ctx, WmpPublishMessage msg) {
        if(msg.getBody().getDirection() == WmpMessageProtos.Direction.TO_SERVER_HANDLER){
            switch (msg.getBody().getTargetType()){
                case CLIENT_ID: handleClientsPublish(msg); break;
                case TOPIC_GENERAL: break;
                default:LOGGER.warn("Target type not supported");
            }
        } else if(msg.getBody().getDirection() == WmpMessageProtos.Direction.TO_CLIENT_SDK){
            switch (msg.getBody().getTargetType()){
                case CLIENT_ID: handlePublishToClient(msg); break;
                default:LOGGER.warn("Target type not supported");
            }
        } else {
            LOGGER.warn("not recognized direction|{}", msg.getBody().getDirection());
        }


//        Channel handlerChannel = handlerChannelManager.get();
    }

    private void handleClientsPublish(WmpPublishMessage publishMessage){
        String[] clientIds = StringUtils.split(publishMessage.getBody().getTarget(),',');
        if(clientIds.length >= 1 && StringUtils.isNotBlank(clientIds[0]) && StringUtils.isNumeric(clientIds[0])) {
            Channel channel = handlerChannelManager.get(clientIds[0]);
            channel.writeAndFlush(publishMessage);
        }else{
            LOGGER.warn("illegal client id|{}", publishMessage.getBody().getTarget());
        }

    }

    private void handlePublishToClient(WmpPublishMessage publishMessage){
        String[] clientIds = StringUtils.split(publishMessage.getBody().getTarget(),',');
        if(clientIds.length >= 1 && StringUtils.isNotBlank(clientIds[0]) && StringUtils.isNumeric(clientIds[0])) {
            Channel channel = sdkChannelManager.getChannel(clientIds[0]);
            if(channel != null && channel.isActive()) {
                channel.writeAndFlush(publishMessage);
            }else{
                LOGGER.warn("channel null or inactive|channel:{}", channel != null ?
                        WmUtils.getChannelRemoteAddress(channel) : null);
            }
        }else{
            LOGGER.warn("illegal client id|{}", publishMessage.getBody().getTarget());
        }

    }

    private void handleTopicGeneralPublish(){

    }
}
