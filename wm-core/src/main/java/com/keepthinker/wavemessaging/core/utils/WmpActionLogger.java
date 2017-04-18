package com.keepthinker.wavemessaging.core.utils;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by keepthinker on 2017/3/26.
 */
public class WmpActionLogger {
    private static final Logger MESSAGE_LOGER = LogManager.getLogger("message");
    private static final Logger CLIENT_LOGGER = LogManager.getLogger("client");

    public static void connect(String clientId, int version){
        CLIENT_LOGGER.trace("CONNECT|clientId:{}|version:{}", clientId, version);
    }

    public void puback(String clientId, long messageId) {
        CLIENT_LOGGER.trace("PUBACK|clientId:{}|messageId:{}", clientId, messageId);
    }
}
