package com.keepthinker.wavemessaging.dao;

import com.keepthinker.wavemessaging.dao.model.ClientInfo;

public interface ClientInfoMapper {
	ClientInfo select(long id);
	void insert(ClientInfo info);
	void delete(long id);
}
