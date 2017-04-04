package com.keepthinker.wavemessaging.redis;

public class RedisUtils {

    private static final String CLIENT_PREFIX = "clt:";
    private static final String USER_NAME_PREFIX = "un:";
    /**
     * Hash: ClientId -- ${field} --> value
     */
    public static String CLIENT_USERNAME = "username";
    public static String CLIENT_PASSWORD = "password";
    public static String CLIENT_TOKEN = "token";
    public static String CLIENT_ACCESS_TIME = "accessTime";
    /** 0: disconnected, 1: online*/
    public static String CLIENT_CONNECTION_STATUS = "connectionStatus";
    public static String CLIENT_DISCONNECT_TIME = "disconnectTime";

    /**
     * Hash: Usernam -- ${field} --> value
     */
    public static String UN_PASSWORD = CLIENT_PASSWORD;
    public static String UN_CLIENT_ID = "clientId";

    public static String GENERAL_STATISTICS = "generalStatistics";
//    public static String GENERAL_STATISTICS_SDK_SIZE = "sdkSize";
//    public static String GENERAL_STATISTICS_HANDLER_SIZE = "handlerSize";

    private static String MESSAGE_PREFIX = "msg:";
    public static String MESSAGE_ID = "messageId";
    public static String MESSAGE_CONTENT = "content";
    public static final String MESSAGE_CREATE_TIME = "createTime";

    private static String CLIENT_MESSAGE_SENDING_PREFIX = "ms:";
    private static String CLIENT_MESSAGE_WAITING_PREFIX = "mw:";


    public static String getClientKey(long clientId) {
        return CLIENT_PREFIX + clientId;
    }

    public static String getClientKey(String clientId) {
        return CLIENT_PREFIX + clientId;
    }

    public static String getUsernameKey(String username) {
        return USER_NAME_PREFIX + username;
    }

    public static String getMessageKey(long msgId){
        return MESSAGE_PREFIX + msgId;
    }
}
