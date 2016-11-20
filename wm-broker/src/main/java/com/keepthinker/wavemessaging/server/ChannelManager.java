package com.keepthinker.wavemessaging.server;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Component;

import io.netty.channel.Channel;

@Component
public class ChannelManager {
	private final HandlerChannelMananger handlerChannelManager = new HandlerChannelMananger();
	
	public static class HandlerChannelMananger{
		
		private final List<Channel> HANDLER_CHANNELS = new CopyOnWriteArrayList<>();
		
		public void add(Channel handlerChannel){
			HANDLER_CHANNELS.add(handlerChannel);
		}
	}

	public HandlerChannelMananger getHandlerChannelMananger(){
		return handlerChannelManager;
	}
	
}
