package com.keepthinker.wavemessaging.client.utils;

import io.netty.channel.Channel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetSocketAddress;

public class WmUtils {
    private static final Logger LOGGER = LogManager.getLogger();

    public static String getAbsolutePath(String relativePath){
        return Thread.currentThread().getContextClassLoader().
                getResource("").getPath() + relativePath;
    }
    public static String getChannelRemoteAddress(Channel channel){
        InetSocketAddress address  = (InetSocketAddress)channel.remoteAddress();
        if(address != null) {
            return address.getHostName() + ":" +  address.getPort();
        }else {
            return null;
        }
    }
}
