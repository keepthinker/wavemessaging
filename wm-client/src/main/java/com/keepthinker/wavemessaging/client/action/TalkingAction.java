package com.keepthinker.wavemessaging.client.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by keepthinker on 2017/4/9.
 */
@Component
public class TalkingAction {

    @Autowired
    private SendMessageAction sendMessageAction;

    public void say(){
        sendMessageAction.sendMessageToItself("Hello, I am talking to myself");
    }
}
