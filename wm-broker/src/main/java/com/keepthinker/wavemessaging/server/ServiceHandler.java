package com.keepthinker.wavemessaging.server;

import com.keepthinker.wavemessaging.core.ProtocolService;
import com.keepthinker.wavemessaging.core.utils.WmUtils;
import com.keepthinker.wavemessaging.core.utils.WmpUtils;
import com.keepthinker.wavemessaging.proto.WmpMessage;
import com.keepthinker.wavemessaging.server.model.ChannelInfo;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * dispatch different type message to corresponding protocal service.<br/>
 * thread safe
 *
 * @author keepthinker
 */
@Service
@Sharable
public class ServiceHandler extends ChannelInboundHandlerAdapter {

    @Resource
    private HandlerChannelMananger handlerChannelManager;

    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    private WmpServiceContainer serviceContainer;

    @Resource
    private SdkChannelManager sdkChannelManager;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        WmpMessage m = (WmpMessage) msg;
        LOGGER.debug("message from clients|method:{}|version:{}", m.getMethod(), m.getVersion());
        ProtocolService<WmpMessage> service = serviceContainer.get(m.getMethod());
        service.handle(ctx, m);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

        ChannelInfo channelInfo = sdkChannelManager.getChannelInfo(ctx.channel());
        LOGGER.debug("channel inactive|remoteAddress:{}|clientId:{}", WmUtils.getChannelRemoteAddress(ctx.channel()), channelInfo.getClientId() );

        handlerChannelManager.get(channelInfo.getClientId()).writeAndFlush
                (WmpUtils.createDisConnectMessage(channelInfo.getClientId()));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOGGER.error("exception caught|remoteAddress:{}|cause:{}", WmUtils.getChannelRemoteAddress(ctx.channel()), cause);
        ctx.close();
    }
}