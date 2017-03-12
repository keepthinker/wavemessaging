package com.keepthinker.wavemessaging.webapi.utils;

import com.keepthinker.wavemessaging.core.utils.Constants;
import com.keepthinker.wavemessaging.core.utils.ZkCommonUtils;

import java.util.List;


/**
 * Created by keepthinker on 2017/1/23.
 */
public class ZkWebApiUtils {
    public static void getServerLessBusy() {
        List<String> brokerNodes = ZkCommonUtils.getChildren(Constants.ZK_BROKER_BASE_PATH);

    }
}
