package com.keepthinker.wavemessaging.server.proto;

import com.keepthinker.wavemessaging.core.ProtocolService;
import com.keepthinker.wavemessaging.proto.WmpConnAckMessage;
import com.keepthinker.wavemessaging.proto.WmpMessageProtos;
import com.keepthinker.wavemessaging.redis.WmStringRedisTemplate;
import com.keepthinker.wavemessaging.server.SDKChannelManager;
import com.keepthinker.wavemessaging.server.ServerStartup;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * handler Handle connack response to decide whether or not to keep or close connection from sdk.
 */
@Service
public class ConnAckService implements ProtocolService<WmpConnAckMessage> {

    @Resource
    private SDKChannelManager sdkChannelManager;

    @Resource
    private ServerStartup serverStartup;

    @Resource
    private WmStringRedisTemplate redisTemplate;

    @Override
    public void handle(ChannelHandlerContext ctx, WmpConnAckMessage msg) {
        if(msg.getBody().getReturnCode().equals(WmpMessageProtos.WmpConnectReturnCode.ACCEPTED)) {

        }else{
            sdkChannelManager.remove(Long.valueOf(msg.getBody().getClientId()));

        }
    }

}
