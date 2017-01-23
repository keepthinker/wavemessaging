package com.keepthinker.wavemessaging.dao;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.keepthinker.wavemessaging.dao.model.ClientInfo;


public class SimpleTest {
	
	@Test
	public void testMapper(){
		ApplicationContext context = new ClassPathXmlApplicationContext("spring-mybatis.xml");

		ClientInfo clientInfo = new ClientInfo();
		clientInfo.setClientId(12123123l);
		clientInfo.setUsername("keepthinker");
		clientInfo.setPassword("wavemessaging");
		ClientInfoMapper mapper = context.getBean(ClientInfoMapper.class);
		mapper.insert(clientInfo);
		System.out.println(clientInfo);
		ClientInfo info = mapper.select(clientInfo.getId());
		System.out.println(info);
		mapper.delete(info.getId());
		
		Assert.assertNotNull(info.getUsername());
		Assert.assertNotNull(info.getPassword());
		Assert.assertNotNull(info.getCreateTime());
		Assert.assertNotNull(info.getUpdateTime());
		Assert.assertTrue(info.getId() > 0);
		Assert.assertTrue(info.getClientId() > 0);
	}

}
