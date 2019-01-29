package com.keepthinker.wavemessaging.client.service;

/**
 * Created by keepthinker on 2017/7/8.
 */
public interface SnsService {

    void sayToMyself(String sentence);

    void sayToGroup(String groupName, String content);

    void createTalkGroup(String groupName);

    void deleteTalkGroup(String groupName);

}
