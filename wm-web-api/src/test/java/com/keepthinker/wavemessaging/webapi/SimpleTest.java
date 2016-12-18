package com.keepthinker.wavemessaging.webapi;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.keepthinker.wavemessaging.webapi.service.GeneralService;

public class SimpleTest {
	
	private ApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
	private Logger log = LogManager.getLogger();
	@Test
	public void testInit(){
		GeneralService gs = context.getBean(GeneralService.class);
		log.error("log log");
	}
}
