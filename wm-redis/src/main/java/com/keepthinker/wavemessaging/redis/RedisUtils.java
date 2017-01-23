package com.keepthinker.wavemessaging.redis;

public class RedisUtils {
	private static final String CLIENT_ID_PREFIX = "ci:";
	public static String getClientIdKey(long clientId){
		return CLIENT_ID_PREFIX + clientId;
	}
	
	public static String getClientIdKey(String clientId){
		return CLIENT_ID_PREFIX + clientId;
	}

	private static final String USER_NAME_PREFIX = "un:";
	public static String getUsernameKey(String username){
		return USER_NAME_PREFIX + username;
	}

	/**	Hash: ClientId -- ${field} --> value*/
	public static String CI_ACCESS_TIME = "accessTime";
	/**	Hash: Usernam -- ${field} --> value*/
	public static String UN_PASSWORD = "password";
	public static String UN_CLIENT_ID = "clientId";
}
