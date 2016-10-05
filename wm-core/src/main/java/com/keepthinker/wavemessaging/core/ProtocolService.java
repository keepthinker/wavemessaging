package com.keepthinker.wavemessaging.core;

import io.netty.channel.ChannelHandlerContext;

public interface ProtocolService<T> {
	void handle(ChannelHandlerContext ctx, T msg);
}
