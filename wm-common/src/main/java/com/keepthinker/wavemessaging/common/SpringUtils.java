package com.keepthinker.wavemessaging.common;

import org.springframework.context.ApplicationContext;

public class SpringUtils {
	private static ApplicationContext context;

	public static ApplicationContext getContext() {
		return context;
	}

	public static void setContext(ApplicationContext context) {
		SpringUtils.context = context;
	}
	
}