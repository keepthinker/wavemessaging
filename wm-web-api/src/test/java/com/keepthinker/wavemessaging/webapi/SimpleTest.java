package com.keepthinker.wavemessaging.webapi;

import com.keepthinker.wavemessaging.core.utils.WmUtils;
import com.keepthinker.wavemessaging.webapi.controller.GeneralController;
import com.keepthinker.wavemessaging.webapi.model.RegisterInfo;
import com.keepthinker.wavemessaging.webapi.service.GeneralService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SimpleTest {


    public void testInit() {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
        Logger log = LogManager.getLogger();
        context.getBean(GeneralController.class);
        GeneralService gs = context.getBean(GeneralService.class);
        RegisterInfo info = new RegisterInfo();
        info.setUsername("keepthinker");
        info.setPassword("123");
        gs.register(info);
        log.error("log log");
    }

    @Test
    public void testWmUtils() {
        long val = WmUtils.generateUniqueId();
        System.out.println(val);
    }

}
