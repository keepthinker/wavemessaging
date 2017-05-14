package com.keepthinker.wavemessaging.client.action;

import com.keepthinker.wavemessaging.client.ChannelHolder;
import com.keepthinker.wavemessaging.client.dao.ClientInfo;
import com.keepthinker.wavemessaging.client.dao.ClientInfoDao;
import com.keepthinker.wavemessaging.client.utils.Constants;
import com.keepthinker.wavemessaging.proto.WmpMessageProtos;
import com.keepthinker.wavemessaging.proto.WmpPublishMessage;
import io.netty.channel.Channel;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by keepthinker on 2017/4/9.
 */
@Component
public class MessageAction {

    private static final Logger LOGGER = org.apache.logging.log4j.LogManager.getLogger();

    @Autowired
    private ChannelHolder holder;

    @Autowired
    private ClientInfoDao clientInfoDao;

    public void sendMessageToItself(String content){
        Channel channel = holder.getChannel();

        if(channel == null){
            LOGGER.warn("not connected to server yet");
            return;
        }
        if(!channel.isActive()){
            LOGGER.warn("it is disconnected");
            return;
        }
        ClientInfo clientInfo = clientInfoDao.get();
        WmpPublishMessage publishMessage = new WmpPublishMessage();
        publishMessage.setVersion(Constants.WMP_VERSION);
        WmpMessageProtos.WmpPublishMessageBody publishMessageBody = WmpMessageProtos.WmpPublishMessageBody.newBuilder()
                .setTarget(String.valueOf(clientInfo.getClientId()))
                .setContent(content)
                .setDirection(WmpMessageProtos.Direction.TO_SERVER_HANDLER)
                .setTargetType(WmpMessageProtos.TargetType.CLIENT_ID)
                .build();

        publishMessage.setBody(publishMessageBody);
        channel.writeAndFlush(publishMessage);
        LOGGER.info("message is sent|", content);
    }

    public void readMessage(String content){

        LOGGER.info("read message|{}", content);
    }
}