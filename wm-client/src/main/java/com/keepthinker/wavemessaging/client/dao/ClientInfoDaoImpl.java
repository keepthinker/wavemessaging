package com.keepthinker.wavemessaging.client.dao;

import com.keepthinker.wavemessaging.core.utils.ClassloaderUtils;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.Properties;

/**
 * mock a dao which access database
 * Created by keepthinker on 2017/1/28.
 */
@Repository
public class ClientInfoDaoImpl implements ClientInfoDao {

    private static final String DATA_FILE = "data.properties";


    private Properties prop = new Properties();

    public ClientInfoDaoImpl() throws IOException {
        prop.load(ClassloaderUtils.getInputStreamFromClasspath(DATA_FILE));
    }

    @Override
    public void insert(ClientInfo clientInfo) {
        prop.setProperty("clientId", String.valueOf(clientInfo.getClientId()));
        prop.setProperty("username", clientInfo.getUsername());
        prop.setProperty("password", clientInfo.getPassword());
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream(Thread.currentThread().getContextClassLoader().getResource(DATA_FILE).toString());
            prop.store(fileOutputStream, "客户端模拟的数据库");
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ClientInfo get() {
        if(prop.get("clientId") == null || prop.get("username") == null || prop.get("password") == null){
            return null;
        }
        String clientIdStr = (String)prop.get("clientId");
        String username = (String)prop.get("username");
        String password = (String)prop.get("password");
        ClientInfo clientInfo = new ClientInfo();
        clientInfo.setClientId(Long.valueOf(clientIdStr));
        clientInfo.setUsername(username);
        clientInfo.setPassword(password);
        return clientInfo;
    }
}
