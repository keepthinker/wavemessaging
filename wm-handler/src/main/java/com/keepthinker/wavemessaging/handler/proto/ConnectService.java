package com.keepthinker.wavemessaging.handler.proto;

import com.keepthinker.wavemessaging.core.ProtocolService;
import com.keepthinker.wavemessaging.handler.ChannelHolder;
import com.keepthinker.wavemessaging.redis.WmStringRedisTemplate;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.mqtt.MqttConnectMessage;
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
        LOGGER.debug("Connack to boroker successfully");


    }

}
