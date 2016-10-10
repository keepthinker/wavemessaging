package com.keepthinker.wavemessaging.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.keepthinker.wavemessaging.core.Constants;
import com.keepthinker.wavemessaging.core.ZookeeperUtils;

@Component
public class HandlersWatcher implements Watcher{

	@Autowired
	private PingTimedTask pingTimedTask;

	private static final Logger LOGGER = LogManager.getLogger();

	@Override
	public void process(WatchedEvent event) {
		LOGGER.info("changes in {}, event's type:{}, state:{}, path:{}", Constants.ZK_BROKER_BASE_PATH, 
				event.getType(), event.getState(), event.getPath());
		//remove inactive handler channel
		pingTimedTask.ping();
		//keep watching handler nodes
		ZookeeperUtils.getChildren(Constants.ZK_BROKER_BASE_PATH, this);
	}
}