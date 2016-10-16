package com.keepthinker.wavemessaging.server.watcher;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.springframework.stereotype.Component;

import com.keepthinker.wavemessaging.core.Constants;
import com.keepthinker.wavemessaging.core.ZookeeperUtils;

/**
 * if the number of handler is decrease, then try to kill this broker handlers' connections if its handlers
 *  connection is above average number of handler brokers holds.
 * @author keepthinker
 */
@Component
public class HandlerWatcher implements Watcher{
	private static final Logger LOGGER = LogManager.getLogger();
	@Override
	public void process(WatchedEvent event) {
		LOGGER.info("changes in {}, event's type:{}, state:{}, path:{}", Constants.ZK_HANDLER_BASE_PATH, 
				event.getType(), event.getState(), event.getPath());
		//remove inactive handler channel and compare other brokers' handler connections,
		//update information in zookeeper,
		//then remove some connections if above average number

		//keep watching handler nodes
		ZookeeperUtils.getChildren(Constants.ZK_HANDLER_BASE_PATH, new HandlerWatcher());
	}
}
