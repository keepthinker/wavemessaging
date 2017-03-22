package com.keepthinker.wavemessaging.client;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.keepthinker.wavemessaging.client.dao.ClientInfo;
import com.keepthinker.wavemessaging.client.dao.ClientInfoDao;
import com.keepthinker.wavemessaging.client.model.ClientWillMessage;
import com.keepthinker.wavemessaging.client.model.LoginResponse;
import com.keepthinker.wavemessaging.client.model.RegisterResponse;
import com.keepthinker.wavemessaging.client.utils.JsonUtils;
import com.keepthinker.wavemessaging.client.utils.PropertiesUtils;
import com.keepthinker.wavemessaging.proto.WmpDecoder;
import com.keepthinker.wavemessaging.proto.WmpEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.mqtt.MqttConnectMessage;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

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
    private void initMqttConnection() {
        b = new Bootstrap();
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

    /**
     * if registered before, return true.<br/>
     * else try to register via http request.<br/>
     */
    private boolean register() {
        if (clientInfoDao.get() != null) {
            return true;
        }

        String username = "username1";
        String password = "password";

        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(PropertiesUtils.getString("web.api.register.url"));

        ObjectNode jsonNode = JsonUtils.OBJECT_MAPPER.createObjectNode();
        jsonNode.put("u", username);
        jsonNode.put("p", password);

        LOGGER.info(jsonNode.toString());
        httpPost.setEntity(new StringEntity(jsonNode.toString(), ContentType.APPLICATION_JSON));

        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpPost);
            LOGGER.info(response.getStatusLine());
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
            LOGGER.error("register failed", e);
            return false;
        } finally {
            return closeHttpQuietly(response);
        }
    }

    /**
     * register --> connect --> ping
     * mock a user's operation
     */
    public void start() {

        if (!register()) {
            LOGGER.error("failed in initializing register operation");
        }
        login();
        initMqttConnection();
        channelManager.setChannel(tcpConnect());
        mqttConnectRequest();

    }

    private boolean login(){
        ClientInfo clientInfo = clientInfoDao.get();
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(PropertiesUtils.getString("web.api.login.url"));

        ObjectNode jsonNode = JsonUtils.OBJECT_MAPPER.createObjectNode();
        jsonNode.put("u", clientInfo.getUsername());
        jsonNode.put("p", clientInfo.getPassword());

        LOGGER.info(jsonNode.toString());
        httpPost.setEntity(new StringEntity(jsonNode.toString(), ContentType.APPLICATION_JSON));

        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpPost);
            LOGGER.info(response.getStatusLine());
            HttpEntity entity = response.getEntity();
            LoginResponse loginResponse = JsonUtils.streamToObject(entity.getContent(), LoginResponse.class);
            if (loginResponse.getData() != null) {
                clientInfo.setToken(loginResponse.getData());
                clientInfoDao.update(clientInfo);
            } else {
                LOGGER.error("http register failed with errorCode: {}, errorMsg: {}", loginResponse.getErrorCode(), loginResponse.getErrorMsg());
                return false;
            }
            EntityUtils.consume(entity);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            return closeHttpQuietly(response);
        }
    }

    private boolean closeHttpQuietly(CloseableHttpResponse response){
        try {
            if(response != null) {
                response.close();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void mqttConnectRequest(){
        ClientInfo clientInfo = clientInfoDao.get();
        clientInfo.getClientId();
        String topic = "all";
        ClientWillMessage willMessage = new ClientWillMessage();
        willMessage.setToken(clientInfo.getToken());
        MqttConnectMessage message =  ClientUtils.createConnectMessage(clientInfo.getClientId()
                , willMessage
                , "topic");
        channelManager.getChannel().writeAndFlush(message);
    }

    public Channel tcpConnect() {
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

}
