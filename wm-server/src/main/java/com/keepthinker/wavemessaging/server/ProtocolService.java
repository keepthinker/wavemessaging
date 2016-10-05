package com.keepthinker.wavemessaging.server;

import io.netty.channel.ChannelHandlerContext;

public interface ProtocolService<T> {
	void handle(ChannelHandlerContext ctx, T msg);
}
