package com.keepthinker.wavemessaging.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.mqtt.MqttMessage;

public interface ProtocolService<T extends MqttMessage> {
    void handle(ChannelHandlerContext ctx, T msg);
}
