package com.keepthinker.wavemessaging.server.model;

import io.netty.channel.Channel;

import java.util.Date;

/**
 * Created by keepthinker on 2017/3/26.
 */
public class ChannelInfo {
    private Channel channel;

    private Date accessTime;

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public Date getAccessTime() {
        return accessTime;
    }

    public void setAccessTime(Date accessTime) {
        this.accessTime = accessTime;
    }
}
