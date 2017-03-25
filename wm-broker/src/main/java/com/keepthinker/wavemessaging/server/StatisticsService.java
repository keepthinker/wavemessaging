package com.keepthinker.wavemessaging.server;

/**
 * Created by keepthinker on 2017/3/24.
 */
public interface StatisticsService {
    /**
     * count number of handlers and SDKs
     */
    void countConnection();
}
