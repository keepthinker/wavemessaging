package com.keepthinker.wavemessaging.client;

import org.springframework.stereotype.Service;

import io.netty.channel.Channel;

@Service
public class ChannelHolder {
	private Channel channel;

	public Channel getChannel() {
		return channel;
	}
	public void setChannel(Channel channel) {
		this.channel = channel;
	}
	
	
}
