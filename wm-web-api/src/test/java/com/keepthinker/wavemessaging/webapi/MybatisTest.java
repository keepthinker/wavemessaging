package com.keepthinker.wavemessaging.webapi;

import com.keepthinker.wavemessaging.dao.ClientInfoMapper;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by keepthinker on 2017/1/2.
 */
public class MybatisTest {
    private ApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");

    @Test
    public void testSelect() {
        ClientInfoMapper clientInfoMapper = context.getBean(ClientInfoMapper.class);
        System.out.println(clientInfoMapper.select(29));
    }
}
