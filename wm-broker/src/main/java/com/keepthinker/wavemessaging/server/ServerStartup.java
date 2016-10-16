package com.keepthinker.wavemessaging.server;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.keepthinker.wavemessaging.core.Constants;
import com.keepthinker.wavemessaging.core.JsonUtils;
import com.keepthinker.wavemessaging.core.ZkServerInfo;
import com.keepthinker.wavemessaging.core.ZookeeperUtils;
import com.keepthinker.wavemessaging.server.watcher.BrokerWatcher;
import com.keepthinker.wavemessaging.server.watcher.HandlerWatcher;

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
	
	@Autowired
	private HandlerWatcher handlerWatcher;
	
	@Autowired
	private BrokerWatcher brokerWatcher;

	/**
	 * register and watch changes in /handlers's children
	 */
	public void zkOperation(){
		boolean result = ZookeeperUtils.createIfNotExisted(Constants.ZK_BROKER_BASE_PATH);
		if(result == false){
			throw new RuntimeException("create a node in zookeeper failed");
		}
		
		ZkServerInfo zkServerInfo = new ZkServerInfo();
		ZookeeperUtils.createEphemeral(Constants.ZK_BROKER_BASE_PATH 
				+ Constants.SIGN_SLASH + Constants.PRIVATE_IP + ":" + port, 
				JsonUtils.objectToString(zkServerInfo));
		
		ZookeeperUtils.watchChildren(Constants.ZK_HANDLER_BASE_PATH, handlerWatcher);

		ZookeeperUtils.watchChildren(Constants.ZK_BROKER_BASE_PATH, brokerWatcher);
	}

	public void run() throws Exception {
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
		ServerStartup startup = context.getBean(ServerStartup.class);
		int port;
		if (args.length > 0) {
			port = Integer.parseInt(args[0]);
			startup.setPort(port);
		}
		startup.run();
	}
}
