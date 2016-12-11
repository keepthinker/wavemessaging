package com.keepthinker.wavemessaging.server;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import io.netty.channel.Channel;

public class HandlerChannelMananger implements ChannelManager{
	private final List<Channel> HANDLER_CHANNELS = new CopyOnWriteArrayList<>();
	
	public void add(Channel handlerChannel){
		HANDLER_CHANNELS.add(handlerChannel);
	}
	
	public int size(){
		return HANDLER_CHANNELS.size();
	}
}
