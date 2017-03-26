package com.keepthinker.wavemessaging.client.proto;

import com.keepthinker.wavemessaging.proto.WmpMessage;
import io.netty.channel.ChannelHandlerContext;

public interface ProtocolService<T extends WmpMessage> {
    void handle(ChannelHandlerContext ctx, T msg);
}
