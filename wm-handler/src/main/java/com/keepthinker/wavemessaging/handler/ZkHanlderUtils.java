package com.keepthinker.wavemessaging.handler;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.springframework.context.ApplicationContext;

import com.keepthinker.wavemessaging.core.Constants;
import com.keepthinker.wavemessaging.core.ZkCommonUtils;

public class ZkHanlderUtils {
	public static void watchBroker(){
		ApplicationContext context = SpringUtils.getContext();
		ChannelHolder channelHolder = context.getBean(ChannelHolder.class);
		ChannelCreater channelCreater = context.getBean(ChannelCreater.class);
		
		
		ZkCommonUtils.createIfNotExisted(Constants.ZK_HANDLER_BASE_PATH);
		ZkCommonUtils.createEphemeral(Constants.ZK_HANDLER_BASE_PATH + Constants.SIGN_SLASH
				+ Constants.PRIVATE_IP);

		ZkCommonUtils.watchChildren(Constants.ZK_BROKER_BASE_PATH, new PathChildrenCacheListener() {

			@Override
			public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
				
				String path = event.getData().getPath();
				String[] strArr = path.split(":");
				String host = strArr[0];
				int port = Integer.valueOf(strArr[1]);
				channelHolder.add(channelCreater.connect(host, port));
				switch(event.getType()){
				case CHILD_ADDED: 
					break;
				case CHILD_REMOVED:; break;
				default:
				}
			}
		});

	}
}
