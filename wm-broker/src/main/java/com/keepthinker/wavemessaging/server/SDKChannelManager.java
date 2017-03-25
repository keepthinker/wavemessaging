package com.keepthinker.wavemessaging.server;

import io.netty.channel.Channel;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Service
public class SDKChannelManager {
    private final Map<Long, Channel> CLIENTID_CHANNEL = new ConcurrentHashMap<>();

    private ReadWriteLock lock = new ReentrantReadWriteLock();
    Lock r = lock.readLock();
    Lock w = lock.writeLock();

    public void put(Long clientId, Channel channel){
        CLIENTID_CHANNEL.put(clientId, channel);
    }

    public int clearInvalid(){
        int count = 0;
        Set<Long> clientIds = CLIENTID_CHANNEL.keySet();
        for(Long clientId : clientIds){
            if(!CLIENTID_CHANNEL.get(clientId).isActive()){
                CLIENTID_CHANNEL.get(clientId).close();
                CLIENTID_CHANNEL.remove(clientId);
                count++;
            }
        }
        return count;
    }

    public void remove(Long clientId){
        CLIENTID_CHANNEL.get(clientId).close();
        CLIENTID_CHANNEL.remove(clientId);
    }

    public int size() {
        return CLIENTID_CHANNEL.values().size();
    }
}
