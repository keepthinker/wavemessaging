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
            forwardToHandler(msg);
        } else if(msg.getBody().getDirection() == WmpMessageProtos.Direction.TO_CLIENT_SDK){
            forwardToClient(msg);
        } else {
            LOGGER.warn("not recognized direction|{}", msg.getBody().getDirection());
        }


//        Channel handlerChannel = handlerChannelManager.get();
    }

    private void forwardToHandler(WmpPublishMessage publishMessage){
        String[] targetClientIds = StringUtils.split(publishMessage.getBody().getTarget(),',');
        String clientId = publishMessage.getBody().getClientId();
        if(targetClientIds.length >= 1 && StringUtils.isNotBlank(clientId)) {
            Channel channel = handlerChannelManager.get(clientId);
            if(channel != null && channel.isActive()) {
                channel .writeAndFlush(publishMessage);
            }else{
                LOGGER.error("channel is null or inactive|channel:{}", channel);
            }
        }else{
            LOGGER.warn("illegal client id|{}", publishMessage.getBody().getTarget());
        }

    }

    private void forwardToClient(WmpPublishMessage publishMessage){
        String clientId = publishMessage.getBody().getTarget();
        if(StringUtils.isNotBlank(clientId)) {
            Channel channel = sdkChannelManager.getChannel(clientId);
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
