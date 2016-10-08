package com.keepthinker.wavemessaging.core;

import java.io.InputStream;

public class WmUtils {
	public static InputStream getInputStreamFromClasspath(String relativePath){

		return Thread.currentThread().getContextClassLoader().getResourceAsStream(relativePath);
	}



}
