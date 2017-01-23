package com.keepthinker.wavemessaging.core.utils;

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
