package com.keepthinker.wavemessaging.client;

import com.keepthinker.wavemessaging.core.MqttUtils;

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
import io.netty.handler.codec.mqtt.MqttFixedHeader;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttMessageType;
import io.netty.handler.codec.mqtt.MqttQoS;

public class ClientStartup {
	private EventLoopGroup workerGroup = new NioEventLoopGroup();

	private Bootstrap b = new Bootstrap();

	private String host;
	private int port;

	public ClientStartup(String host, int port){
		this.host = host;
		this.port = port;
		init();
	}
	
	public void init(){
		b.group(workerGroup);
		b.channel(NioSocketChannel.class);
		b.option(ChannelOption.SO_KEEPALIVE, true);
		b.handler(new ChannelInitializer<SocketChannel>() {
			@Override
			public void initChannel(SocketChannel ch) throws Exception {
				ch.pipeline().addLast(new MqttDecoder(), new ServiceHandler(), MqttEncoder.INSTANCE);
			}
		});
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
		String host = null;
		int port = 0;
		if(args.length == 2){
			host = args[0];
			port = Integer.parseInt(args[1]);
		}else{
			throw new RuntimeException("host port not set");
		}
		ClientStartup clientStartup = new ClientStartup(host, port);
		Channel channel = clientStartup.connect();
		channel.writeAndFlush(MqttUtils.getPingReqMessage());
		channel.closeFuture().sync();
	}
	
}
