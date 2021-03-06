package com.keepthinker.wavemessaging.core.utils;

import java.io.IOException;
import java.util.Properties;

public class PropertiesUtils {
    private static final Properties PROPERTIES = new Properties();

    static {
        try {
            PROPERTIES.load(ClassloaderUtils.getInputStreamFromClasspath("wm.properties"));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    public static String getString(String key) {
        return PROPERTIES.getProperty(key);
    }

    public static int getInt(String key) {
        return Integer.valueOf(PROPERTIES.getProperty(key));
    }

    public static int getInt(String key, int defaultValue) {
        try {
            return Integer.valueOf(PROPERTIES.getProperty(key));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return defaultValue;
        }
    }
}
