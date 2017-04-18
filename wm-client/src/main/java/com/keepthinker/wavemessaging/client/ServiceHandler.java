package com.keepthinker.wavemessaging.client;

import com.keepthinker.wavemessaging.client.proto.ProtocolService;
import com.keepthinker.wavemessaging.client.utils.WmUtils;
import com.keepthinker.wavemessaging.proto.WmpMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    private WmpServiceContainer serviceContainer;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        WmpMessage m = (WmpMessage) msg;
        ProtocolService<WmpMessage> service = serviceContainer.get(m.getMethod());
        service.handle(ctx, m);
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOGGER.error("exception caught|remoteAddress:{}|cause:{}", WmUtils.getChannelRemoteAddress(ctx.channel()), cause);
        ctx.close();
    }

}
