package com.keepthinker.wavemessaging.handler;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.keepthinker.wavemessaging.core.Constants;
import com.keepthinker.wavemessaging.core.MqttUtils;
import com.keepthinker.wavemessaging.core.ZookeeperUtils;

import io.netty.channel.Channel;

@Component
public class PingTimedTask {
	private static final Logger LOGGER = LogManager.getLogger();
	
	@Autowired
	private HandlerStartup handlerStartup;

	@Autowired
	private ChannelHolder channelManager;
	public void ping(){
		Channel channel = channelManager.getChannel();
		if(channel == null){
			LOGGER.warn("channel is null, retry later");
			return;
		}
		if(channel.isActive() == false){
			LOGGER.warn("handler-server channel is inactive");
			//try to find a new active broker, if not at present ,wait for few minutes and check again
			tryToConnectToActiveServer();
			return;
		}
		channel.writeAndFlush(MqttUtils.getPingReqMessage());
	}
	
	private void tryToConnectToActiveServer(){
		List<String> brokerPath = ZookeeperUtils.getChildren(Constants.ZK_BROKER_BASE_PATH);
		
	}
}
