package com.keepthinker.wavemessaging.server;


import com.keepthinker.wavemessaging.core.utils.SpringUtils;
import com.keepthinker.wavemessaging.proto.WmpDecoder;
import com.keepthinker.wavemessaging.proto.WmpEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ServerStartup {

    private static final Logger LOGGER = LogManager.getLogger();

    private int port;
    @Autowired
    private ServiceHandler serviceHandler;

    public ServerStartup(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
        SpringUtils.setContext(context);
        ServerStartup startup = context.getBean(ServerStartup.class);
        int port;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
            startup.setPort(port);
        }
        startup.run();
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void run() throws Exception {
        LOGGER.info("Broker is going to run");
        EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap(); // (2)
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class) // (3)
                    .childHandler(new ChannelInitializer<SocketChannel>() { // (4)
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(WmpEncoder.INSTANCE, new WmpDecoder(), serviceHandler);
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)          // (5)
                    .childOption(ChannelOption.SO_KEEPALIVE, true); // (6)

            // Bind and start to accept incoming connections.
            ChannelFuture f = b.bind(port);

            f.sync(); // (7)

            LOGGER.info("Broker has started with port {}", port);

            ZkBrokerUtils.registerBroker(port);
            // Wait until the server socket is closed.
            // In this example, this does not happen, but you can do that to gracefully
            // shut down your server.
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }

    }
}
