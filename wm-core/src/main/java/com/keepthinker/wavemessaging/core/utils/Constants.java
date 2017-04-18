package com.keepthinker.wavemessaging.core.utils;

public class Constants {
    public static final String DEFAULT_CHARSET = "UTF-8";

    public static final String ZK_BROKER_BASE_PATH = "/brokers";

    public static final String ZK_HANDLER_BASE_PATH = "/handlers";

    public static final String SIGN_SLASH = "/";
    public static final String SIGN_COLON = ":";

    public static final String NODE_NAME_HANDLER = "handler";
    public static final String NODE_NAME_SERVER = "server";

    public static final String CLIENT_ID_PREFIX_HANDLER = "handler:";

    public static final String MIME_TYPE_APPLICATION_JSON = "application/json;charset=utf-8";

    public static final long MESSAGE_DEFAULT_TIMEOUT = 1000 * 3600 * 24 * 30l;

    public static int CONNECTION_STATUTS_ONLINE = 1;
    public static int CONNECTION_STATUTS_DISCONNECTED = 0;
}
