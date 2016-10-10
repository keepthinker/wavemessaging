package com.keepthinker.wavemessaging.handler;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.keepthinker.wavemessaging.core.Constants;
import com.keepthinker.wavemessaging.core.ZookeeperUtils;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.mqtt.MqttDecoder;
import io.netty.handler.codec.mqtt.MqttEncoder;

public class HandlerStartup {
	private static final Logger LOGGER = LogManager.getLogger();
	@Autowired
	private ChannelHolder channelManager;

	@Autowired
	private ServiceHandler serviceHandler;

	private EventLoopGroup workerGroup = new NioEventLoopGroup();

	private Bootstrap b = new Bootstrap();

	private String host;
	private int port;

	public HandlerStartup(String host, int port){
		this.host = host;
		this.port = port;
		init();
	}

	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}

	public void init(){
		b.group(workerGroup);
		b.channel(NioSocketChannel.class);
		b.option(ChannelOption.SO_KEEPALIVE, true);
		b.handler(new ChannelInitializer<SocketChannel>() {
			@Override
			public void initChannel(SocketChannel ch) throws Exception {
				ch.pipeline().addLast(MqttEncoder.INSTANCE, new MqttDecoder(), serviceHandler);
			}
		});
	}

	@Autowired
	private HandlersWatcher handlersWatcher;
	
	public void zkOperation(){
		ZookeeperUtils.createIfNotExisted(Constants.ZK_HANDLER_BASE_PATH);
		ZookeeperUtils.createEphemeral(Constants.ZK_HANDLER_BASE_PATH + Constants.SIGN_SLASH
				+ Constants.PRIVATE_IP);
		ZookeeperUtils.watchChildren(Constants.ZK_BROKER_BASE_PATH, handlersWatcher);
	}
	

	
	public void start(){
		List<String> brokerPath = ZookeeperUtils.getChildren(Constants.ZK_BROKER_BASE_PATH);
		
		channelManager.setChannel(connect());

		zkOperation();
	}

	public Channel connect(){
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



	public static void main(String[] args) throws Exception {

		ApplicationContext context =  new ClassPathXmlApplicationContext("spring.xml");
		HandlerStartup startup = context.getBean(HandlerStartup.class);

		startup.start();


	}

}
