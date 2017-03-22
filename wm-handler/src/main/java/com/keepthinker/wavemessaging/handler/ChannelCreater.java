package com.keepthinker.wavemessaging.handler;

import com.keepthinker.wavemessaging.proto.WmpDecoder;
import com.keepthinker.wavemessaging.proto.WmpEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class ChannelCreater {
    @Autowired
    private ServiceHandler serviceHandler;

    private EventLoopGroup workerGroup = new NioEventLoopGroup();

    private Bootstrap b = new Bootstrap();

    @PostConstruct
    public void init() {
        b.group(workerGroup);
        b.channel(NioSocketChannel.class);
        b.option(ChannelOption.SO_KEEPALIVE, true);
        b.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(WmpEncoder.INSTANCE, new WmpDecoder(), serviceHandler);
            }
        });
    }

    public Channel connect(String host, int port) {
        ChannelFuture f = null;
        try {
            f = b.connect(host, port).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
        Channel channel = f.channel();
        return channel;
    }
}
