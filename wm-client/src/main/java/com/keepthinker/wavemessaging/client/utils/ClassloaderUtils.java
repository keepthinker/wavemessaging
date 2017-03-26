package com.keepthinker.wavemessaging.client.utils;

import java.io.InputStream;

public class ClassloaderUtils {
    public static InputStream getInputStreamFromClasspath(String relativePath) {

        return Thread.currentThread().getContextClassLoader().
                getResourceAsStream(relativePath);
    }

}
