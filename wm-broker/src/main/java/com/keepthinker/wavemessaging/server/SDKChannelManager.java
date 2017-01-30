package com.keepthinker.wavemessaging.server;

import io.netty.channel.Channel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class SDKChannelManager {
    private final List<Channel> SDK_CHANNELS = new CopyOnWriteArrayList<>();

    public void add(Channel handlerChannel) {
        SDK_CHANNELS.add(handlerChannel);
    }

    public int size() {
        return SDK_CHANNELS.size();
    }
}
