package com.keepthinker.wavemessaging.handler.proto;

import com.keepthinker.wavemessaging.core.ProtocolService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.mqtt.MqttConnAckMessage;

/**
 * Created by keepthinker on 2017/1/27.
 */
public class ConnackService implements ProtocolService<MqttConnAckMessage> {
    @Override
    public void handle(ChannelHandlerContext ctx, MqttConnAckMessage msg) {

    }
}
