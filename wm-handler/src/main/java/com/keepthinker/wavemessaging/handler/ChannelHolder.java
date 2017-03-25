package com.keepthinker.wavemessaging.handler;

import com.keepthinker.wavemessaging.core.utils.WmUtils;
import io.netty.channel.Channel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class ChannelHolder {
    private static final Logger LOGGER = LogManager.getLogger();

    private List<Channel> handlerBrokerChannels = new ArrayList<>();

    public List<Channel> getHandlerBrokerChannels() {
        return handlerBrokerChannels;
    }

    public void add(Channel channel) {
        synchronized (handlerBrokerChannels) {
            InetSocketAddress socketAddress = (InetSocketAddress) channel.remoteAddress();
            String host = socketAddress.getHostName();
            int port = socketAddress.getPort();
            handlerBrokerChannels.add(channel);
            LOGGER.info("Add a channel whose host is {}, and port is {}", host, port);
        }
    }

    public void remove(String host, int port) {
        synchronized (handlerBrokerChannels) {
            for (Iterator<Channel> channelIterator = handlerBrokerChannels.iterator(); channelIterator.hasNext(); ) {
                Channel channel = channelIterator.next();
                InetSocketAddress socketAddress = (InetSocketAddress) channel.remoteAddress();
                if (socketAddress.getAddress().getHostAddress().equals(host)
                        && socketAddress.getPort() == port) {
                    channelIterator.remove();
                    channel.close();
                    LOGGER.info("Remove a channel whose host is {}, and port is {}", host, port);
                }
            }
        }
    }

    public int clearInvalid(){
        synchronized (handlerBrokerChannels) {
            return WmUtils.clearInvalidChannel(handlerBrokerChannels);
        }
    }

}
