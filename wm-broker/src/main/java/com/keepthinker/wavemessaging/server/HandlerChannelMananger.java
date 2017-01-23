package com.keepthinker.wavemessaging.server;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import io.netty.channel.Channel;

public class HandlerChannelMananger{
	private final List<Channel> HANDLER_CHANNELS = new ArrayList<>();

	private ReadWriteLock lock = new ReentrantReadWriteLock();
	Lock r = lock.readLock();
	Lock w = lock.writeLock();

	public void add(Channel handlerChannel){
		w.lock();
		try{
			HANDLER_CHANNELS.add(handlerChannel);
		}finally{
			w.unlock();
		}
	}

	public int size(){
		return HANDLER_CHANNELS.size();
	}

	public Channel get(String clientId){
		r.lock();
		try{
			int hashValue = hash(clientId);
			return HANDLER_CHANNELS.get(hashValue % HANDLER_CHANNELS.size());
		}finally{
			r.unlock();
		}
	}

	private int hash(String str){
		char[] value = str.toCharArray();
		int hash = 0;
		int h = hash;
		if (h == 0 && value.length > 0) {
			char val[] = value;

			for (int i = 0; i < value.length; i++) {
				h = 31 * h + val[i];
			}
			hash = h;
		}
		return h;
	}
}
