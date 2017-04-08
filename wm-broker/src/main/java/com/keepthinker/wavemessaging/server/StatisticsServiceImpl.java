package com.keepthinker.wavemessaging.server;

import com.keepthinker.wavemessaging.core.utils.WmUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by keepthinker on 2017/3/24.
 */
@Service
public class StatisticsServiceImpl implements StatisticsService{

    @Autowired
    private HandlerChannelMananger  handlerChannelMananger;

    @Autowired
    private SdkChannelManager sdkChannelManager;

    @Autowired
    private ServerStartup serverStartup;

    public void countConnection(){
        int numOfHandlers = handlerChannelMananger.size();
        int numOfSdks = sdkChannelManager.size();
        ZkBrokerUtils.setZkServerInfo(WmUtils.getIPV4Private(), serverStartup.getPort(), numOfHandlers, numOfSdks);

    }
}
