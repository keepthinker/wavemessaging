package com.keepthinker.wavemessaging.handler.proto;

import com.keepthinker.wavemessaging.core.ProtocolService;
import com.keepthinker.wavemessaging.handler.ChannelHolder;
import com.keepthinker.wavemessaging.handler.MessageIdGenerator;
import com.keepthinker.wavemessaging.proto.WmpMessageProtos;
import com.keepthinker.wavemessaging.proto.WmpPublishMessage;
import com.keepthinker.wavemessaging.redis.RedisUtils;
import com.keepthinker.wavemessaging.redis.WmStringShardRedisTemplate;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by keepthinker on 2017/4/4.
 */
@Service
public class PublishService implements ProtocolService<WmpPublishMessage> {

    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    private MessageIdGenerator messageIdGenerator;

    @Autowired
    private WmStringShardRedisTemplate shardRedisTemplate;

    @Autowired
    private ChannelHolder channelHolder;

    @Override
    public void handle(ChannelHandlerContext ctx, WmpPublishMessage msg) {
        if(msg.getBody().getDirection() == WmpMessageProtos.Direction.TO_SERVER_HANDLER){
            switch (msg.getBody().getTargetType()){
                case CLIENT_ID: handleClientsPublish(msg); break;
                case TOPIC_GENERAL: break;
                default:LOGGER.warn("Target  type not supported");
            }
        } else if(msg.getBody().getDirection() == WmpMessageProtos.Direction.TO_CLIENT_SDK){

        } else {
            LOGGER.warn("not recognized direction|{}", msg.getBody().getDirection());
        }
    }

    private void handleClientsPublish(WmpPublishMessage msg){
        WmpMessageProtos.WmpPublishMessageBody body = msg.getBody();
        String[] clientIds = StringUtils.split(body.getTarget(),',');
        for(int i = 0; i < clientIds.length; i++){
            //record message info in redis and mysql
            long newMsgId = messageIdGenerator.generate();
            Map<String, String> map = new HashMap<>();
            map.put(RedisUtils.MESSAGE_ID, String.valueOf(newMsgId));
            map.put(RedisUtils.MESSAGE_CONTENT, body.getContent());
            map.put(RedisUtils.MESSAGE_CREATE_TIME, String.valueOf(new Date().getTime()));
            shardRedisTemplate.hmset(RedisUtils.getMessageKey(newMsgId), map);

            //send if online
            shardRedisTemplate.set(RedisUtils.getClientMessageSendingKey(newMsgId), String.valueOf(newMsgId));
            String brokerPrivateAddress = shardRedisTemplate.hget(RedisUtils.getClientKey(clientIds[i]), RedisUtils.CLIENT_BROKER_PRIVATE_ADDRESS);
            Channel brokerChannel = channelHolder.getChannel(brokerPrivateAddress);

            msg.setBody(body.toBuilder().setTarget(clientIds[i]).build());
            brokerChannel.writeAndFlush(msg);
            //set in sending cache or append to waiting list if not online

        }
    }
}
