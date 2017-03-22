package com.keepthinker.wavemessaging.client;

import com.keepthinker.wavemessaging.proto.WmpMessage;
import io.netty.channel.ChannelHandlerContext;

public interface ProtocolService<T extends WmpMessage> {
    void handle(ChannelHandlerContext ctx, T msg);
}
