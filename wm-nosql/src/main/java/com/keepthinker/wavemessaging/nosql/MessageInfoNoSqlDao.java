package com.keepthinker.wavemessaging.nosql;

import com.keepthinker.wavemessaging.nosql.redis.model.MessageInfo;

/**
 * Created by keepthinker on 2017/4/9.
 */
public interface MessageInfoNoSqlDao {
    void save(MessageInfo messageInfo);

//    void get(long messageId);
}
