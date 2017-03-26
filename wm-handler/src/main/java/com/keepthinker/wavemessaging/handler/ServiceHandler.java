package com.keepthinker.wavemessaging.handler;

import com.keepthinker.wavemessaging.core.ProtocolService;
import com.keepthinker.wavemessaging.proto.WmpMessage;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Sharable
@Service
public class ServiceHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOGGER = LogManager.getLogger();
    @Autowired
    private WmpServiceContainer serviceContainer;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        WmpMessage m = (WmpMessage) msg;
        LOGGER.debug("message from server|{}|{}", m.getMethod(), m.getVersion());
        ProtocolService<WmpMessage> service = serviceContainer.get(m.getMethod());
        service.handle(ctx, m);
    }

}
