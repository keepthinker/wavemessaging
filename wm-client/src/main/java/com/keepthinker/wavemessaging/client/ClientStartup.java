package com.keepthinker.wavemessaging.client;

import com.keepthinker.wavemessaging.client.dao.ClientInfo;
import com.keepthinker.wavemessaging.client.dao.ClientInfoDao;
import com.keepthinker.wavemessaging.core.utils.JsonUtils;
import com.keepthinker.wavemessaging.core.utils.PropertiesUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.mqtt.MqttDecoder;
import io.netty.handler.codec.mqtt.MqttEncoder;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class ClientStartup {

    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    private ChannelHolder channelManager;

    @Autowired
    private ServiceHandler serviceHandler;

    private EventLoopGroup workerGroup = new NioEventLoopGroup();

    private Bootstrap b;

    private String host;
    private int port;
    @Autowired
    private ClientInfoDao clientInfoDao;

    public ClientStartup(String host, int port) {
        this.host = host;
        this.port = port;
        init();
    }

    public static void main(String[] args) throws Exception {

        ApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
        ClientStartup startup = context.getBean(ClientStartup.class);

        if (args.length == 2) {
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

    /**
     * Create tcp keep alive connection via netty.<br/>
     * Protocol: MQTT
     */
    private void initMqtt() {
        b = new Bootstrap();
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

    /**
     * if registered before, return true.<br/>
     * else try to register via http request.<br/>
     */
    private boolean initRegister() {
        if (clientInfoDao.get() != null) {
            return true;
        }

        String username = "username";
        String password = "password";

        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(PropertiesUtils.getString("web.api.host"));
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("username", username));
        nvps.add(new BasicNameValuePair("password", password));
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpPost);
            System.out.println(response.getStatusLine());
            HttpEntity entity = response.getEntity();
            // do something useful with the response body
            // and ensure it is fully consumed
            RegisterResponse registerResponse = JsonUtils.streamToObject(entity.getContent(), RegisterResponse.class);
            if (registerResponse.getData() != null) {
                long clientId = registerResponse.getData();
                ClientInfo clientInfo = new ClientInfo();
                clientInfo.setClientId(clientId);
                clientInfo.setUsername(username);
                clientInfo.setPassword(password);
                clientInfoDao.insert(clientInfo);
            } else {
                LOGGER.error("http register failed with errorCode: {}, errorMsg: {}", registerResponse.getErrorCode(), registerResponse.getErrorMsg());
                return false;
            }
            EntityUtils.consume(entity);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public void init() {
        if (initRegister()) {
            initMqtt();
        }
    }

    /**
     * mock a user's operation
     */
    public void start() {
        channelManager.setChannel(connect());

        ClientInfo clientInfo = clientInfoDao.get();
        if (clientInfo == null) {
            //register

        } else {

        }
        //Check if clientId, username, password
        //Try to connect
        //if failed register

    }

    public Channel connect() {
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
