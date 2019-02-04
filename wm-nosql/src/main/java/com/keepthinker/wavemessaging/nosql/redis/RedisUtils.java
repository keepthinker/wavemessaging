package com.keepthinker.wavemessaging.nosql.redis;

public class RedisUtils {

    private static final String CLIENT_PREFIX = "clt:";
    private static final String USER_NAME_PREFIX = "un:";
    /**
     * Hash: ClientId -- ${field} --> value
     */
    public static final String CLIENT_USERNAME = "username";
    public static final String CLIENT_PASSWORD = "password";
    public static final String CLIENT_TOKEN = "token";
    public static final String CLIENT_ACCESS_TIME = "accessTime";
    /** 0: disconnected, 1: online*/
    public static final String CLIENT_CONNECTION_STATUS = "connectionStatus";
    public static final String CLIENT_DISCONNECT_TIME = "disconnectTime";
    public static final String CLIENT_BROKER_PUBLIC_ADDRESS = "brokerPublicAddress";
    public static final String CLIENT_BROKER_PRIVATE_ADDRESS = "brokerPrivateAddress";

    /**
     * Hash: Usernam -- ${field} --> value
     */
    public static final String UN_PASSWORD = CLIENT_PASSWORD;
    public static final String UN_CLIENT_ID = "clientId";

    public static final String GENERAL_STATISTICS = "generalStatistics";
//    public static final String GENERAL_STATISTICS_SDK_SIZE = "sdkSize";
//    public static final String GENERAL_STATISTICS_HANDLER_SIZE = "handlerSize";

    private static final String MESSAGE_PREFIX = "msg:";
    public static final String MESSAGE_ID = "messageId";
    public static final String MESSAGE_CONTENT = "content";
    public static final String MESSAGE_CREATE_TIME = "createTime";
    public static final String MESSAGE_TIMEOUT = "timeout";
    public static final String MESSAGE_TARGET_TYPE = "targetType";
    public static final String MESSAGE_TARGET = "target";

    private static final String CLIENT_MESSAGE_SENDING_PREFIX = "cms:";
    private static final String CLIENT_MESSAGE_WAITING_PREFIX = "cmw:";

    private static final String TOPIC_GENERAL_PREFIX = "topic:general:";
    /** scatter topic clientIds to different bucket */
    public static final int TOPIC_BUCKET_SIZE = 128;


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

    public static String getClientMessageSendingKey(String clientId){
        return CLIENT_MESSAGE_SENDING_PREFIX + clientId;
    }

    public static String getClientMessageWaitingKey(String clientId){
        return CLIENT_MESSAGE_WAITING_PREFIX + clientId;
    }


    public static String getTopicGeneralPrefix(String topicGeneral, long clientId) {
        return new StringBuilder(64)
                .append(TOPIC_GENERAL_PREFIX)
                .append(topicGeneral)
                .append(":")
                .append(clientId % TOPIC_BUCKET_SIZE).toString();
    }

}
