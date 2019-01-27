package com.keepthinker.wavemessaging.client.service.impl;

import com.keepthinker.wavemessaging.client.service.MessageService;
import com.keepthinker.wavemessaging.client.service.SnsService;
import com.keepthinker.wavemessaging.client.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by keepthinker on 2017/4/9.
 */
@Component("snsService")
public class SnsServiceImpl implements SnsService {

    @Autowired
    private MessageService messageAction;

    @Autowired
    private TopicService topicAction;

    @Override
    public void sayToMyself(String sentence){
        messageAction.sendMessageToItself(sentence);
    }

    @Override
    public void createTalkGroup(String groupName){
        topicAction.createTopic(groupName);
    }

    @Override
    public void deleteTalkGroup(String groupName) {
        topicAction.createTopic(groupName);
    }
}
