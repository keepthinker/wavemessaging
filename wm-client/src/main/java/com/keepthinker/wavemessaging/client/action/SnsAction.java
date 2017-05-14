package com.keepthinker.wavemessaging.client.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by keepthinker on 2017/4/9.
 */
@Component("snsAction")
public class SnsAction {

    @Autowired
    private MessageAction messageAction;

    @Autowired
    private TopicAction topicAction;

    public void say(){
        messageAction.sendMessageToItself("Hello, I am talking to myself");
    }

    public void createTopic(){
        topicAction.createTopic();
    }
}
