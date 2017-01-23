package com.keepthinker.wavemessaging.handler;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.keepthinker.wavemessaging.core.utils.MqttUtils;

import io.netty.channel.Channel;

@Component
public class PingTimedTask {
	private static final Logger LOGGER = LogManager.getLogger();

	@Autowired
	private ChannelHolder channelHolder;
	public void ping(){
		List<Channel> channels = channelHolder.getHandlerBrokerChannels();
		for(Channel channel : channels){
			if(channel == null){
				LOGGER.warn("channel is null, retry later");
				return;
			}
			if(channel.isActive() == false){
				channel.close();
				LOGGER.warn("handler-server channel is inactive");
				//try to find a new active broker, if not at present ,wait for few minutes and check again
				tryToConnectToActiveServer();
				return;
			}
			channel.writeAndFlush(MqttUtils.PINGREQ);
		}
	}

	private void tryToConnectToActiveServer(){

	}
}
