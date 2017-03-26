package com.keepthinker.wavemessaging.server;

import com.keepthinker.wavemessaging.core.ProtocolService;
import com.keepthinker.wavemessaging.proto.WmpMessage;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * dispatch different type message to corresponding protocal service.<br/>
 * thread safe
 *
 * @author keepthinker
 */
@Service
@Sharable
public class ServiceHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    private WmpServiceContainer serviceContainer;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        WmpMessage m = (WmpMessage) msg;
        LOGGER.debug("message from clients|{}|{}", m.getMethod(), m.getVersion());
        ProtocolService<WmpMessage> service = serviceContainer.get(m.getMethod());
        service.handle(ctx, m);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}