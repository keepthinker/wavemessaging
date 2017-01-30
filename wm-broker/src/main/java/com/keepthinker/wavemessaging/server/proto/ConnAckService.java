package com.keepthinker.wavemessaging.server.proto;

import com.keepthinker.wavemessaging.core.ClientType;
import com.keepthinker.wavemessaging.core.ProtocolService;
import com.keepthinker.wavemessaging.core.utils.WmUtils;
import com.keepthinker.wavemessaging.core.utils.ZkCommonUtils;
import com.keepthinker.wavemessaging.server.SDKChannelManager;
import com.keepthinker.wavemessaging.server.ServerStartup;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.mqtt.MqttConnAckMessage;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Handle connack response from handler to decide whether or not to keep or close connection from sdk.
 */
@Service
public class ConnAckService implements ProtocolService<MqttConnAckMessage> {

    @Resource
    private SDKChannelManager sdkChannelManager;

    @Resource
    private ServerStartup serverStartup;

    @Override
    public void handle(ChannelHandlerContext ctx, MqttConnAckMessage msg) {
        sdkChannelManager.add(ctx.channel());
        ZkCommonUtils.increaseZkServerInfo(WmUtils.getIPV4Private(), serverStartup.getPort(), ClientType.SDK);
    }

}
