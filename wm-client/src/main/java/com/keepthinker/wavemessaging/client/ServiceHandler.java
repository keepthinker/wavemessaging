package com.keepthinker.wavemessaging.client;

import com.keepthinker.wavemessaging.client.proto.ProtocolService;
import com.keepthinker.wavemessaging.proto.WmpMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceHandler extends ChannelInboundHandlerAdapter {
    @Autowired
    private WmpServiceContainer serviceContainer;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        WmpMessage m = (WmpMessage) msg;
        ProtocolService<WmpMessage> service = serviceContainer.get(m.getMethod());
        service.handle(ctx, m);
    }

}
