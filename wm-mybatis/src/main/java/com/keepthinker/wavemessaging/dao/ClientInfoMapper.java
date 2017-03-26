package com.keepthinker.wavemessaging.dao;

import com.keepthinker.wavemessaging.dao.model.ClientInfo;
import org.apache.ibatis.annotations.Param;

public interface ClientInfoMapper {
    ClientInfo select(long id);

    void insert(ClientInfo info);

    void delete(long id);

    ClientInfo selectByClientId(@Param("clientId") Long clientId);

    ClientInfo selectByUsername(@Param("username") String username);
}
