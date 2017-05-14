package com.keepthinker.wavemessaging.server.proto;

import com.keepthinker.wavemessaging.core.ProtocolService;
import com.keepthinker.wavemessaging.core.utils.WmUtils;
import com.keepthinker.wavemessaging.proto.WmpSubAckMessage;
import com.keepthinker.wavemessaging.server.SdkChannelManager;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by keepthinker on 2017/5/6.
 */
@Service
public class SubAckService implements ProtocolService<WmpSubAckMessage> {

    private static final Logger LOGGER = LogManager.getLogger();

    @Resource
    private SdkChannelManager sdkChannelManager;

    @Override
    public void handle(ChannelHandlerContext ctx, WmpSubAckMessage msg) {

        String clientId = msg.getBody().getClientId();
        if(StringUtils.isNotBlank(clientId)) {
            Channel channel = sdkChannelManager.getChannel(clientId);
            if(channel != null && channel.isActive()) {
                channel.writeAndFlush(msg);
            }else{
                LOGGER.warn("channel null or inactive|channel:{}", channel != null ?
                        WmUtils.getChannelRemoteAddress(channel) : null);
            }
        }else{
            LOGGER.warn("illegal client id|{}", clientId);
        }
    }

}
