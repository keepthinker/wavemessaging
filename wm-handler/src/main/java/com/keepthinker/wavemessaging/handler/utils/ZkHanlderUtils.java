package com.keepthinker.wavemessaging.handler.utils;

import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.keepthinker.wavemessaging.common.Constants;
import com.keepthinker.wavemessaging.core.ChildrenChangeListener;
import com.keepthinker.wavemessaging.core.ZkCommonUtils;
import com.keepthinker.wavemessaging.handler.ChannelCreater;
import com.keepthinker.wavemessaging.handler.ChannelHolder;
import com.keepthinker.wavemessaging.handler.SpringUtils;

import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.MqttConnectMessage;

public class ZkHanlderUtils {
	
	private static final Logger LOGGER = LogManager.getLogger();
	
	public static void watchZkBrokers(){
		final ChannelCreater creater = SpringUtils.getContext().getBean(ChannelCreater.class);
		final ChannelHolder holder = SpringUtils.getContext().getBean(ChannelHolder.class);
		ZkCommonUtils.watchChildren(Constants.ZK_BROKER_BASE_PATH, new ChildrenChangeListener() {
			@Override
			public void removed(PathChildrenCacheEvent event) {
				String serverAddr = ZkCommonUtils.get1LevelPath(event.getData().getPath());
				LOGGER.info("removed {}", serverAddr);
				String[] arr = serverAddr.split(":");
				String host = arr[0];
				int port = Integer.valueOf(arr[1]);
				holder.remove(host, port);
			}

			@Override
			public void added(PathChildrenCacheEvent event) {
				String serverAddr = ZkCommonUtils.get1LevelPath(event.getData().getPath());
				LOGGER.info("added {}", serverAddr);
				String[] arr = serverAddr.split(":");
				String host = arr[0];
				int port = Integer.valueOf(arr[1]);
				
				Channel channel = creater.connect(host, port);
				
				MqttConnectMessage connectMessage = HandlerUtils.HANDLER_CONNECT_MESSAGE;
				channel.writeAndFlush(connectMessage);
			}
		});
	}
	
}
