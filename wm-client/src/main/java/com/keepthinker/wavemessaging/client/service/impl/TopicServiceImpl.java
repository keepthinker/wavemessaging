package com.keepthinker.wavemessaging.client.service.impl;

import com.keepthinker.wavemessaging.client.ChannelHolder;
import com.keepthinker.wavemessaging.client.dao.ClientInfo;
import com.keepthinker.wavemessaging.client.dao.ClientInfoDao;
import com.keepthinker.wavemessaging.client.service.TopicService;
import com.keepthinker.wavemessaging.proto.WmpMessageProtos;
import com.keepthinker.wavemessaging.proto.WmpSubscribeMessage;
import com.keepthinker.wavemessaging.proto.WmpUnsubscribeMessage;
import io.netty.channel.Channel;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * Created by keepthinker on 2017/5/7.
 */
@Component
public class TopicServiceImpl implements TopicService{

    private static final Logger LOGGER = org.apache.logging.log4j.LogManager.getLogger();

    @Autowired
    private ChannelHolder holder;

    @Autowired
    private ClientInfoDao clientInfoDao;

    public void createTopic(String topicName){
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

        Random random = new Random();

        WmpSubscribeMessage subscribeMessage = new WmpSubscribeMessage();
        WmpMessageProtos.WmpSubscribeMessageBody body = WmpMessageProtos.WmpSubscribeMessageBody.newBuilder()
                .setTargetType(WmpMessageProtos.TargetType.TOPIC_GENERAL)
                .setClientId(String.valueOf(clientInfo.getClientId()))
                .setSubscribeId(random.nextInt(Integer.MAX_VALUE))
                .addTopics(topicName)
                .build();
        subscribeMessage.setBody(body);

        holder.getChannel().writeAndFlush(subscribeMessage);

        LOGGER.info("send subscribe message|subscribeId:{}|topics:{}", body.getSubscribeId(), body.getTopicsList());
    }

    @Override
    public void deleteTopic(String topicName) {
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

        Random random = new Random();

        WmpUnsubscribeMessage unsubscribeMessage = new WmpUnsubscribeMessage();
        WmpMessageProtos.WmpUnsubscribeMessageBody body = WmpMessageProtos.WmpUnsubscribeMessageBody.newBuilder()
                .setTargetType(WmpMessageProtos.TargetType.TOPIC_GENERAL)
                .setClientId(String.valueOf(clientInfo.getClientId()))
                .setUnsubscribeId(random.nextInt(Integer.MAX_VALUE))
                .addTopics(topicName)
                .build();
        unsubscribeMessage.setBody(body);

        holder.getChannel().writeAndFlush(unsubscribeMessage);

        LOGGER.info("send subscribe message|subscribeId:{}|topics:{}", body.getUnsubscribeId(), body.getTopicsList());
    }
}
