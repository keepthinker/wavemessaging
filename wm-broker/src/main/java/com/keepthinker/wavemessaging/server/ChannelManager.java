package com.keepthinker.wavemessaging.server;

import io.netty.channel.Channel;

public interface ChannelManager {
	
	void add(Channel channel);
	Channel get();
	int size();
}
