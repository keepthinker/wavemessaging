package com.keepthinker.wavemessaging.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by keepthinker on 2017/3/24.
 */
@Component
public class StatisticsTimedTask {

    @Autowired
    private StatisticsService statisticsService;

    public void countConnection(){
        statisticsService.countConnection();
    }

}
