package com.keepthinker.wavemessaging.dao;

import com.keepthinker.wavemessaging.dao.model.ClientInfo;

public interface ClientInfoMapper {
	ClientInfo get(long id);
	void save(ClientInfo info);
	void delete(long id);
}
