package com.keepthinker.wavemessaging.handler;

import com.keepthinker.wavemessaging.core.utils.SpringUtils;
import com.keepthinker.wavemessaging.handler.utils.ZkHanlderUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class HandlerStartup {
    private static final Logger LOGGER = LogManager.getLogger();

    public static void main(String[] args) throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
        SpringUtils.setContext(context);
        ZkHanlderUtils.watchZkBrokers();
        LOGGER.info("the handler get started");
    }

}
