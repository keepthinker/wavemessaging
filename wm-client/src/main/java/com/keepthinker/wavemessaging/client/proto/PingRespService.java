package com.keepthinker.wavemessaging.client.proto;

import com.keepthinker.wavemessaging.client.utils.WmUtils;
import com.keepthinker.wavemessaging.proto.WmpMessage;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class PingRespService implements ProtocolService<WmpMessage> {
    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void handle(ChannelHandlerContext ctx, WmpMessage msg) {
        LOGGER.info("receive PINGRESP reponse from server|{}", WmUtils.getChannelRemoteAddress(ctx.channel()));
    }

}
