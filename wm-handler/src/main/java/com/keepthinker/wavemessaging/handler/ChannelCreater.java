package com.keepthinker.wavemessaging.handler;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

@Component
public class ChannelCreater {
	@Autowired
	private ServiceHandler serviceHandler;

	private EventLoopGroup workerGroup = new NioEventLoopGroup();

	private Bootstrap b = new Bootstrap();

	@PostConstruct
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

	public Channel connect(String host, int port){
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
