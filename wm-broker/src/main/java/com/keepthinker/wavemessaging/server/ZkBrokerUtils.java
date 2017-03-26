package com.keepthinker.wavemessaging.server;

import com.keepthinker.wavemessaging.core.model.ZkServerInfo;
import com.keepthinker.wavemessaging.core.utils.Constants;
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

    /**
     * set clientNum, handlerNum, handler is also a client
     *
     * @param host
     * @param port
     */
    public static void setZkServerInfo(String host, int port, int numOfHandlers, int numOfSdks) {
        //Presuming node's state change is not concurrent, Locks is considered in future
        String path = new StringBuilder(32).append(Constants.ZK_BROKER_BASE_PATH)
                .append(Constants.SIGN_SLASH )
                .append(host)
                .append(Constants.SIGN_COLON)
                .append(port).toString();

        ZkServerInfo zkServerInfo = new ZkServerInfo();
        zkServerInfo.setNumOfSdkChannels(numOfSdks);
        zkServerInfo.setNumOfHandlerChannels(numOfHandlers);
        ZkCommonUtils.setIfExsited(path, JsonUtils.objectToString(zkServerInfo));
    }


}
