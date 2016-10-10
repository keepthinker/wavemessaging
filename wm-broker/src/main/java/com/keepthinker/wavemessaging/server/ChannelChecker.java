package com.keepthinker.wavemessaging.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class ChannelChecker {
	private static final Logger LOGGER = LogManager.getLogger();
	
	public void checkValidity(){
		
		LOGGER.info("checking channel");
		//remove inactive channel
	}
}
