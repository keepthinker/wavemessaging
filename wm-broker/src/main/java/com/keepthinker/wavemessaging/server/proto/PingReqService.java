package com.keepthinker.wavemessaging.server.proto;

import com.keepthinker.wavemessaging.core.ProtocolService;
import com.keepthinker.wavemessaging.core.utils.WmUtils;
import com.keepthinker.wavemessaging.core.utils.WmpUtils;
import com.keepthinker.wavemessaging.proto.WmpPingReqMessage;
import com.keepthinker.wavemessaging.server.SdkChannelManager;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by keepthinker on 2017/3/26.
 */
@Service
public class PingReqService implements ProtocolService<WmpPingReqMessage> {

    @Autowired
    private SdkChannelManager sdkChannelManager;

    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void handle(ChannelHandlerContext ctx, WmpPingReqMessage msg) {
        LOGGER.info("receive ping request|{}", WmUtils.getChannelRemoteAddress(ctx.channel()));
        ctx.channel().writeAndFlush(WmpUtils.PINGRESP);
        sdkChannelManager.getChannelInfo(ctx.channel()).setAccessTime(new Date());
    }
}
