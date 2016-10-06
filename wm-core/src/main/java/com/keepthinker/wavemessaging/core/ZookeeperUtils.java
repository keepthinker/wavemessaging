package com.keepthinker.wavemessaging.core;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.data.Stat;

public class ZookeeperUtils {
	private static final CuratorFramework CURATOR_FRAMEWORK;
	static {
		CURATOR_FRAMEWORK = CuratorUtils.createSimple(PropertiesUtils.getString("zookeeper.connectionString"));

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

}
