package com.keepthinker.wavemessaging.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ChannelChecker {
    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    private HandlerChannelMananger handlerChannelMananger;

    @Autowired
    private SDKChannelManager sdkChannelManager;

    public void clearInvalid() {

        int clearHandlerCount = handlerChannelMananger.clearInvalid();
        int clearsSdkCount = sdkChannelManager.clearInvalid();
        LOGGER.info("remove handler channel size:{}, sdk channel size:{}",
                clearHandlerCount, clearsSdkCount);

    }
}
