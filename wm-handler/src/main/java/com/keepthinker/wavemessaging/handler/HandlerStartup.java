package com.keepthinker.wavemessaging.handler;

import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.keepthinker.wavemessaging.core.ChildrenChangeListener;
import com.keepthinker.wavemessaging.core.Constants;
import com.keepthinker.wavemessaging.core.ZkCommonUtils;

import io.netty.channel.Channel;

public class HandlerStartup {
	private static final Logger LOGGER = LogManager.getLogger();

	public static void main(String[] args) throws Exception {
		ApplicationContext context =  new ClassPathXmlApplicationContext("spring.xml");
		SpringUtils.setContext(context);
		HandlerStartup startup = new HandlerStartup();
		startup.watchZkBrokers();

	}

	private void watchZkBrokers(){
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
				//					ChannelInfo info = new ChannelInfo();
				//					info.setHost(host);
				//					info.setPort(port);
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
				holder.add(channel);

			}
		});
	}

}
