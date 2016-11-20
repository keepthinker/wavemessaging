package com.keepthinker.wavemessaging.server;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.keepthinker.wavemessaging.common.Constants;
import com.keepthinker.wavemessaging.common.JsonUtils;
import com.keepthinker.wavemessaging.common.SpringUtils;
import com.keepthinker.wavemessaging.common.WmUtils;
import com.keepthinker.wavemessaging.core.ZkCommonUtils;
import com.keepthinker.wavemessaging.core.ZkServerInfo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.mqtt.MqttDecoder;
import io.netty.handler.codec.mqtt.MqttEncoder;

public class ServerStartup {
	
	private static final Logger LOGGER = LogManager.getLogger();
	private int port;

	public ServerStartup(int port) {
		this.port = port;
	}
	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	@Autowired
	private ServiceHandler serviceHandler;
	

	/**
	 * register and watch changes in /handlers's children
	 */
	public void zkOperation(){
		boolean result = ZkCommonUtils.createIfNotExisted(Constants.ZK_BROKER_BASE_PATH);
		if(result == false){
			throw new RuntimeException("create a node in zookeeper failed");
		}
		
		ZkServerInfo zkServerInfo = new ZkServerInfo();
		ZkCommonUtils.createEphemeral(Constants.ZK_BROKER_BASE_PATH 
				+ Constants.SIGN_SLASH + WmUtils.getIPV4Private() + ":" + port, 
				JsonUtils.objectToString(zkServerInfo));
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
					ch.pipeline().addLast(MqttEncoder.INSTANCE, new MqttDecoder(), serviceHandler);
				}
			})
			.option(ChannelOption.SO_BACKLOG, 128)          // (5)
			.childOption(ChannelOption.SO_KEEPALIVE, true); // (6)

			// Bind and start to accept incoming connections.
			ChannelFuture f = b.bind(port);

			zkOperation();
			
			f.sync(); // (7)
			LOGGER.info("Broker has started with port {}", port);
			// Wait until the server socket is closed.
			// In this example, this does not happen, but you can do that to gracefully
			// shut down your server.
			f.channel().closeFuture().sync();
		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}

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
}
