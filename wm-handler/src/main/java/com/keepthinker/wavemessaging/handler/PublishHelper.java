package com.keepthinker.wavemessaging.handler;

import com.keepthinker.wavemessaging.nosql.ClientInfoNoSqlDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by keepthinker on 2017/4/16.
 */
@Component
public class PublishHelper {
    @Autowired
    private ClientInfoNoSqlDao clientInfoNoSqlDao;



}
