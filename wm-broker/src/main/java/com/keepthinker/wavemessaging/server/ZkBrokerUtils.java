package com.keepthinker.wavemessaging.server;

import com.keepthinker.wavemessaging.core.utils.Constants;
import com.keepthinker.wavemessaging.core.model.ZkServerInfo;
import com.keepthinker.wavemessaging.core.utils.JsonUtils;
import com.keepthinker.wavemessaging.core.utils.WmUtils;
import com.keepthinker.wavemessaging.core.utils.ZkCommonUtils;

public class ZkBrokerUtils {


    /**
     * register broker address in zookeeper, which will be discovered by other
     * nodes(handler for example)
     */
    public static void registerBroker(int port) {
        boolean result = ZkCommonUtils.createIfNotExisted(Constants.ZK_BROKER_BASE_PATH);
        if (result == false) {
            throw new RuntimeException("create a node in zookeeper failed");
        }

        ZkServerInfo zkServerInfo = new ZkServerInfo();
        ZkCommonUtils.createEphemeral(Constants.ZK_BROKER_BASE_PATH
                        + Constants.SIGN_SLASH + WmUtils.getIPV4Private() + ":" + port,
                JsonUtils.objectToString(zkServerInfo));
    }

    public static boolean isClientIdFromSDK(){
        return false;
    }
}
