package com.keepthinker.wavemessaging.client;

import io.netty.channel.Channel;
import org.springframework.stereotype.Service;

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
