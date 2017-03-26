package com.keepthinker.wavemessaging.client.utils;

import com.keepthinker.wavemessaging.proto.WmpPingReqMessage;
import com.keepthinker.wavemessaging.proto.WmpPingRespMessage;

public class WmpUtils {

    public static final WmpPingReqMessage PINGREQ = new WmpPingReqMessage(Constants.WMP_VERSION);

    public static final WmpPingRespMessage PINGRESP = new WmpPingRespMessage(Constants.WMP_VERSION);

}
