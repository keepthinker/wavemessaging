package com.keepthinker.wavemessaging.core;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.mqtt.MqttMessage;

public interface ProtocolService<T extends MqttMessage> {
    void handle(ChannelHandlerContext ctx, T msg);
}
