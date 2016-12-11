package com.keepthinker.wavemessaging.server;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import io.netty.channel.Channel;

public class SDKChannelManager implements ChannelManager{
private final List<Channel> SDK_CHANNELS = new CopyOnWriteArrayList<>();
	
	public void add(Channel handlerChannel){
		SDK_CHANNELS.add(handlerChannel);
	}
	
	public int size(){
		return SDK_CHANNELS.size();
	}
}
