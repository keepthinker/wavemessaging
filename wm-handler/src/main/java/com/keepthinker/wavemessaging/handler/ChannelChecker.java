package com.keepthinker.wavemessaging.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ChannelChecker {
    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    private ChannelHolder channelHolder;

    public void clearInvalid() {

        LOGGER.info("checking channel");
        int clearCount = channelHolder.clearInvalid();
        LOGGER.info("remove handler channel size:{}",
                clearCount);

    }
}
