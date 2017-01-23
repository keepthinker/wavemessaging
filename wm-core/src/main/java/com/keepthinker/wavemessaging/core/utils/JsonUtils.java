package com.keepthinker.wavemessaging.core.utils;

import java.io.IOException;

import com.keepthinker.wavemessaging.core.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * wrap Json class, in order to easily change different Json implementation
 * @author keepthinker
 *
 */
public class JsonUtils {
	private static final Logger LOGGER = LogManager.getLogger();
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	public static String objectToString(Object obj){
		try {
			return OBJECT_MAPPER.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			LOGGER.error(e);
			return null;
		}
	}

	public static <T> T stringToObject(String obj, Class<T> clazz){
		try {
			return OBJECT_MAPPER.readValue(obj.getBytes(Constants.DEFAULT_CHARSET), clazz);
		} catch (IOException e) {
			LOGGER.error(e);
			return null;
		}
	}
}
