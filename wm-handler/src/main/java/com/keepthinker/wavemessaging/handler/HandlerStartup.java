package com.keepthinker.wavemessaging.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.keepthinker.wavemessaging.handler.utils.ZkHanlderUtils;

public class HandlerStartup {
	private static final Logger LOGGER = LogManager.getLogger();

	public static void main(String[] args) throws Exception {
		ApplicationContext context =  new ClassPathXmlApplicationContext("spring.xml");
		SpringUtils.setContext(context);
		ZkHanlderUtils.watchZkBrokers();
	}

}
