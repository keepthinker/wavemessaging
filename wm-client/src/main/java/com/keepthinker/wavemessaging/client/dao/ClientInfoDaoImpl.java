package com.keepthinker.wavemessaging.client.dao;

import com.keepthinker.wavemessaging.client.utils.ClassloaderUtils;
import com.keepthinker.wavemessaging.client.utils.WmUtils;
import org.apache.commons.lang.StringUtils;
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
        new File(WmUtils.getAbsolutePath(DATA_FILE)).createNewFile();
        prop.load(ClassloaderUtils.getInputStreamFromClasspath(DATA_FILE));
    }

    @Override
    public void insert(ClientInfo clientInfo) {
        if(clientInfo.getClientId() != 0)
            prop.setProperty("clientId", String.valueOf(clientInfo.getClientId()));
        if(StringUtils.isNotBlank(clientInfo.getUsername()))
            prop.setProperty("username", clientInfo.getUsername());
        if(StringUtils.isNotBlank(clientInfo.getPassword()))
            prop.setProperty("password", clientInfo.getPassword());
        if(StringUtils.isNotBlank(clientInfo.getToken()))
            prop.setProperty("token", clientInfo.getToken());
        OutputStreamWriter os = null;
        try {
            String filePath = Thread.currentThread().getContextClassLoader().getResource(DATA_FILE).getPath().toString();
            File dataFile = new File(filePath);
            if(!dataFile.exists()){
                dataFile.createNewFile();
            }
            os = new OutputStreamWriter(new FileOutputStream(filePath),  "UTF-8");
            prop.store(os, "客户端模拟的数据库");
            os.flush();
        }catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            if(os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void update(ClientInfo clientInfo) {
        insert(clientInfo);
    }

    @Override
    public ClientInfo get() {
        if(StringUtils.isBlank((String)prop.get("clientId")) ||
                StringUtils.isBlank((String)prop.get("username"))||
                StringUtils.isBlank((String)prop.get("password"))){
            return null;
        }
        String clientIdStr = (String)prop.get("clientId");
        String username = (String)prop.get("username");
        String password = (String)prop.get("password");
        String token = (String)prop.get("token");
        ClientInfo clientInfo = new ClientInfo();
        clientInfo.setClientId(Long.valueOf(clientIdStr));
        clientInfo.setUsername(username);
        clientInfo.setPassword(password);
        clientInfo.setToken(token);
        return clientInfo;
    }
}
