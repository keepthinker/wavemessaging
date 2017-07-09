package com.keepthinker.wavemessaging.handler;

import com.keepthinker.wavemessaging.core.ConcurrentTool;
import com.keepthinker.wavemessaging.core.ProtocolService;
import com.keepthinker.wavemessaging.core.ThreadRejectedListener;
import com.keepthinker.wavemessaging.core.utils.WmUtils;
import com.keepthinker.wavemessaging.handler.proto.RejectRequestService;
import com.keepthinker.wavemessaging.proto.WmpConnectMessage;
import com.keepthinker.wavemessaging.proto.WmpMessage;
import com.keepthinker.wavemessaging.proto.WmpMessageMethod;
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

    private ConcurrentTool concurrentTool = ConcurrentTool.newBuilder().setThreadAbortListener(new ServiceUnavailableResponseListener()).build();

    @Autowired
    private WmpServiceContainer serviceContainer;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        WmpMessage wmpMessage = (WmpMessage) msg;
        LOGGER.debug("message from server|{}|{}", wmpMessage.getMethod(), wmpMessage.getVersion());
        concurrentTool.execute(new ChannelTask(serviceContainer, ctx, wmpMessage));
    }

    public static class ChannelTask implements Runnable{
        private ChannelHandlerContext channelHandlerContext;
        private WmpMessage wmpMessage;
        private WmpServiceContainer serviceContainer;

        public ChannelTask(WmpServiceContainer serviceContainer, ChannelHandlerContext ctx, WmpMessage msg) {
            this.channelHandlerContext = ctx;
            this.wmpMessage = msg;
            this.serviceContainer = serviceContainer;
        }

        @Override
        public void run() {
            serviceContainer.get(wmpMessage.getMethod()).handle(channelHandlerContext, wmpMessage);
        }

        public ChannelHandlerContext getChannelHandlerContext() {
            return channelHandlerContext;
        }

        public WmpMessage getWmpMessage() {
            return wmpMessage;
        }

        public WmpServiceContainer getServiceContainer() {
            return serviceContainer;
        }
    }


    public static class ServiceUnavailableResponseListener implements ThreadRejectedListener{
        //TO-DO
        @Override
        public void abortEvent(Runnable runnable) {
            LOGGER.error("service is busy, can't accept more task");
            if(runnable instanceof ChannelTask){
                // send back service busy request to tell client to stop requesting server till it's available again
                ChannelTask channelTask = (ChannelTask) runnable;
                if(channelTask.getWmpMessage().getMethod() == WmpMessageMethod.CONNECT){
                    WmpConnectMessage wmpConnectMessage = (WmpConnectMessage)channelTask.getWmpMessage();
                    wmpConnectMessage.getBody().getClientId();
                    //TO-DO
                    RejectRequestService service;
                    //......
                }
            }
        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOGGER.error("exception caught|remoteAddress:" + WmUtils.getChannelRemoteAddress(ctx.channel()), cause);
    }
}
