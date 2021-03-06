package com.keepthinker.wavemessaging.nosql;

import com.keepthinker.wavemessaging.nosql.redis.model.MessageInfo;
import static com.keepthinker.wavemessaging.proto.WmpMessageProtos.*;

/**
 * Created by keepthinker on 2017/4/9.
 */
public interface MessageInfoNoSqlDao {
    void save(MessageInfo messageInfo);

    MessageInfo getPartialForPublish(long messageId);

    long expire(String key, int seconds);
}
