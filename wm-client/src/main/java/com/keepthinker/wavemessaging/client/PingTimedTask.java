package com.keepthinker.wavemessaging.client;

import com.keepthinker.wavemessaging.client.utils.WmpUtils;
import io.netty.channel.Channel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PingTimedTask {
    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    private ChannelHolder channelManager;

    public void ping() {
        Channel channel = channelManager.getChannel();
        if (channel == null) {
            LOGGER.warn("channel is null, retry later");
            return;
        }
        if (channel.isActive() == false) {
            System.out.println("cilent channel is inactive");
            return;
        }
        channel.writeAndFlush(WmpUtils.PINGREQ);
    }
}
