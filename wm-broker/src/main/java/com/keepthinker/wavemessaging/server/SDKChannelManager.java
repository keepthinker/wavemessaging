package com.keepthinker.wavemessaging.server;

import io.netty.channel.Channel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class SDKChannelManager {
    private final List<Channel> SDK_CHANNELS = new CopyOnWriteArrayList<>();

    private final Map<Long, Channel> CLIENTID_CHANNEL = new ConcurrentHashMap<>();

    public void add(Channel handlerChannel) {
        SDK_CHANNELS.add(handlerChannel);
    }

    public void put(Long clientId, Channel channel){
        CLIENTID_CHANNEL.put(clientId, channel);
    }


    public int size() {
        return SDK_CHANNELS.size();
    }
}
