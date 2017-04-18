package com.keepthinker.wavemessaging.nosql.redis.model;

import java.util.Date;

/**
 * Created by keepthinker on 2017/4/9.
 */
public class MessageInfo {
    private long id;
    private String content;
    private long timeout;
    private Date createTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
