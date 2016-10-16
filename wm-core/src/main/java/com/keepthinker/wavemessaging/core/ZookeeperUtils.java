package com.keepthinker.wavemessaging.core;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.data.Stat;

public class ZookeeperUtils {
	private static final Logger LOGGER = LogManager.getLogger();
	private static final CuratorFramework CURATOR_FRAMEWORK;
	static {
		CURATOR_FRAMEWORK = CuratorUtils.createSimple(PropertiesUtils.getString("zookeeper.connectionString")
				,PropertiesUtils.getString("zookeeper.namespace"));

		CURATOR_FRAMEWORK.start();
	}
	public static String get(String path){
		try {
			return new String(CURATOR_FRAMEWORK.getData().forPath(path), Constants.DEFAULT_CHARSET);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Stat set(String path, String data){
		try {
			return CURATOR_FRAMEWORK.setData().forPath(path, data.getBytes(Constants.DEFAULT_CHARSET));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static boolean delete(String path){
		try {
			CURATOR_FRAMEWORK.delete().forPath(path);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean create(String path){
		try {
			CURATOR_FRAMEWORK.create().forPath(path);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * return true if it is created successfully or existed before
	 * @param path
	 * @return
	 */
	public static boolean createIfNotExisted(String path){
		try {
			Stat stat = CURATOR_FRAMEWORK.checkExists().forPath(path);
			if(stat == null){
				try {
					CURATOR_FRAMEWORK.create().forPath(path);
				} catch (Exception e1) {
					LOGGER.warn(e1.getMessage());
					return false;
				}
			}
			return true;
		} catch (Exception e) {
			LOGGER.warn(e);
			return false;
		}
	}

	public static boolean createEphemeral(String path){
		try {
			CURATOR_FRAMEWORK.create().withMode(CreateMode.EPHEMERAL).forPath(path);
			return true;
		} catch (Exception e) {
			LOGGER.warn(e);
			return false;
		}
	}

	public static boolean createEphemeral(String path, byte[] data){
		try {
			CURATOR_FRAMEWORK.create().withMode(CreateMode.EPHEMERAL).forPath(path, data);
			return true;
		} catch (Exception e) {
			LOGGER.warn(e);
			return false;
		}
	}

	public static void createEphemeral(String path, String objectToString) {
		try {
			CURATOR_FRAMEWORK.create().withMode(CreateMode.EPHEMERAL).forPath(path, objectToString
					.getBytes(Constants.DEFAULT_CHARSET));
		} catch (Exception e) {
			LOGGER.warn(e);
		}	
	}

	public static void watchChildren(String path, Watcher watcher){
		try {
			CURATOR_FRAMEWORK.getChildren().usingWatcher(watcher).forPath(path);
		} catch (Exception e) {
			LOGGER.warn(e);
		}
	}

	public static List<String> getChildren(String path, Watcher watcher){
		try {
			return CURATOR_FRAMEWORK.getChildren().usingWatcher(watcher).forPath(path);
		} catch (Exception e) {
			LOGGER.warn(e);
			return null;
		}
	}

	public static List<String> getChildren(String path){
		try {
			return CURATOR_FRAMEWORK.getChildren().forPath(path);
		} catch (Exception e) {
			LOGGER.warn(e);
			return null;
		}
	}

	/**
	 * increase clientNum, handlerNum
	 * @param host
	 * @param port
	 */
	public static void increaseZkServerInfo(String host, int port, ClientType type){
		//Presuming node's state change is not concurrent, Locks is considered in future
		String path = Constants.ZK_BROKER_BASE_PATH
				+ Constants.SIGN_SLASH + host +  Constants.SIGN_COLON + port;
		String info = ZookeeperUtils.get(path);
		ZkServerInfo zkServerInfo;
		if(StringUtils.isNotBlank(info)){
			zkServerInfo = JsonUtils.stringToObject(info, ZkServerInfo.class);
			zkServerInfo.setClientNum(zkServerInfo.getClientNum() + 1);
			if(type == ClientType.HANDLER){
				zkServerInfo.setHandlerNum(zkServerInfo.getHandlerNum() + 1);
			}
		}else{
			zkServerInfo = new ZkServerInfo();
			zkServerInfo.setClientNum(1);
			if(type == ClientType.HANDLER){
				zkServerInfo.setHandlerNum(1);
			}
		}
		ZookeeperUtils.set(path, JsonUtils.objectToString(zkServerInfo));
	}

}
