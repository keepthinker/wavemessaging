package com.keepthinker.wavemessaging.handler;

import com.keepthinker.wavemessaging.core.utils.WmUtils;
import com.keepthinker.wavemessaging.core.utils.WmpUtils;
import io.netty.channel.Channel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PingTimedTask {
    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    private ChannelHolder channelHolder;

    public void ping() {
        List<Channel> channels = channelHolder.getHandlerBrokerChannels();
        for (Channel channel : channels) {
            if (channel == null) {
                LOGGER.warn("channel is null, retry later");
                return;
            }
            if (channel.isActive() == false) {
                channel.close();
                LOGGER.warn("handler-server channel is inactive|{}", WmUtils.getChannelRemoteAddress(channel));
                //try to find a new active broker, if not at present ,wait for few minutes and check again
                tryToConnectToActiveServer();
                return;
            }
            LOGGER.info("handler send ping request to broker|{}", WmUtils.getChannelRemoteAddress(channel));
            channel.writeAndFlush(WmpUtils.PINGREQ);
        }
    }

    private void tryToConnectToActiveServer() {

    }
}
