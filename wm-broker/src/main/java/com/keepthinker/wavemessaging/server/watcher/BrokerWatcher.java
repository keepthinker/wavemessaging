package com.keepthinker.wavemessaging.server.watcher;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.springframework.stereotype.Component;

import com.keepthinker.wavemessaging.core.Constants;
import com.keepthinker.wavemessaging.core.JsonUtils;
import com.keepthinker.wavemessaging.core.ZkServerInfo;
import com.keepthinker.wavemessaging.core.ZookeeperUtils;

/**
 * if the number of broker is increased, then try to kill this broker handlers' connections if its handlers connection is 
 * above average number of handler brokers holds.
 * @author keepthinker
 *
 */
@Component
public class BrokerWatcher implements Watcher{
	private static final Logger LOGGER = LogManager.getLogger();

	@Override
	public void process(WatchedEvent event) {
		LOGGER.info("changes in {}, event's type:{}, state:{}, path:{}", Constants.ZK_BROKER_BASE_PATH, 
				event.getType(), event.getState(), event.getPath());
		
		List<String> brokers = ZookeeperUtils.getChildren(Constants.ZK_BROKER_BASE_PATH);
		int handlerNum = 0;
		for(String addr : brokers){
			ZkServerInfo info = JsonUtils.stringToObject(ZookeeperUtils.get(Constants.ZK_BROKER_BASE_PATH + Constants.SIGN_SLASH + addr)
					, ZkServerInfo.class);
			handlerNum += info.getHandlerNum(); 
		}
		int avgHandlerNum = handlerNum / brokers.size();
		
		ZkServerInfo thisServerInfo = JsonUtils.stringToObject(
				ZookeeperUtils.get(Constants.ZK_BROKER_BASE_PATH + Constants.SIGN_SLASH + Constants.PRIVATE_IP)
				, ZkServerInfo.class);
		if(thisServerInfo.getHandlerNum() > avgHandlerNum){
			// kill and remove handlers, let them connect to new added broker
			LOGGER.info("kill handlers' connection in {}, event's type:{}, state:{}, path:{}", Constants.ZK_BROKER_BASE_PATH, 
					event.getType(), event.getState(), event.getPath());
			
		}
		
		//keep watching broker nodes
		ZookeeperUtils.getChildren(Constants.ZK_BROKER_BASE_PATH, new HandlerWatcher());
	}

}
