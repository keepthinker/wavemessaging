package com.keepthinker.wavemessaging.server;

import com.keepthinker.wavemessaging.server.model.ChannelInfo;
import io.netty.channel.Channel;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SDKChannelManager {
    private final Map<String, Channel> CLIENTID_CHANNEL = new ConcurrentHashMap<>();
    private final Map<Channel, ChannelInfo> CHANNEL_CHANNELINFO = new ConcurrentHashMap<>();

    public ChannelInfo getChannelInfo(Channel channel){
        return CHANNEL_CHANNELINFO.get(channel);
    }

    public ChannelInfo getChannelInfo(String clientId){
        return getChannelInfo(getChannel(clientId));
    }

    public void putChannel(String clientId, Channel channel){
        CLIENTID_CHANNEL.put(clientId, channel);

        ChannelInfo channelInfo = new ChannelInfo();
        channelInfo.setAccessTime(new Date());
        CHANNEL_CHANNELINFO.put(channel, channelInfo);
    }

    public Channel getChannel(String clientId) {
        return CLIENTID_CHANNEL.get(clientId);
    }

    public int clearInvalid(){
        int count = 0;
        Set<String> clientIds = CLIENTID_CHANNEL.keySet();
        for(String clientId : clientIds){
            if(!CLIENTID_CHANNEL.get(clientId).isActive()){
                remove(clientId);
                count++;
            }
        }
        return count;
    }

    public void remove(String clientId){
        Channel channel = CLIENTID_CHANNEL.get(clientId);
        CHANNEL_CHANNELINFO.remove(channel);
        CLIENTID_CHANNEL.remove(clientId);
        channel.close();
    }

    public int size() {
        return CLIENTID_CHANNEL.values().size();
    }
}
