package com.keepthinker.wavemessaging.core;

import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.data.Stat;

public class ZookeeperUtils {
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

	public static boolean createEphemeral(String path){
		try {
			CURATOR_FRAMEWORK.create().withMode(CreateMode.EPHEMERAL).forPath(path);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean register(String path){
		return createEphemeral(path);

	}

	public static List<String> getChildren(String path, Watcher watcher){
		try {
			return CURATOR_FRAMEWORK.getChildren().usingWatcher(watcher).forPath(path);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
