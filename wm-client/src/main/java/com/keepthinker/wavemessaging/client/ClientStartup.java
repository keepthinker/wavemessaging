package com.keepthinker.wavemessaging.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

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

public class ClientStartup {
	
	@Autowired
	private ChannelHolder channelManager;

	@Autowired
	private ServiceHandler serviceHandler;
	
	private EventLoopGroup workerGroup = new NioEventLoopGroup();

	private Bootstrap b = new Bootstrap();

	private String host;
	private int port;

	public ClientStartup(String host, int port){
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
	
	public void start(){
		channelManager.setChannel(connect());
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
		ClientStartup startup = context.getBean(ClientStartup.class);

		if(args.length == 2){
			String host = args[0];
			int port = Integer.parseInt(args[1]);
			startup.setHost(host);
			startup.setPort(port);
		}
		startup.start();
//		ChannelManager manager = context.getBean(ChannelManager.class);
//		manager.setChannel(channel);
//		for(;;){
//			if(channel.isActive() == false){
//				System.out.println("cilent channel is inactive");
//			}
//			channel.writeAndFlush(MqttUtils.getPingReqMessage());
//			Thread.sleep(PropertiesUtils.getInt("ping.time.interval", 10));
//
//		}
//		channel.closeFuture().sync();

	}

}
