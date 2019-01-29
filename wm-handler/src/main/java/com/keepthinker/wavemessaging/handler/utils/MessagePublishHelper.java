package com.keepthinker.wavemessaging.handler.utils;

import com.keepthinker.wavemessaging.core.utils.Constants;
import com.keepthinker.wavemessaging.handler.ChannelHolder;
import com.keepthinker.wavemessaging.nosql.ClientInfoNoSqlDao;
import com.keepthinker.wavemessaging.nosql.ClientMessageSendingNoSqlDao;
import com.keepthinker.wavemessaging.nosql.ClientMessageWaitingNoSqlDao;
import com.keepthinker.wavemessaging.nosql.MessageInfoNoSqlDao;
import com.keepthinker.wavemessaging.proto.WmpMessageProtos;
import com.keepthinker.wavemessaging.proto.WmpPublishMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by keepthinker on 2017/4/23.
 */
@Component
public class MessagePublishHelper {

    @Autowired
    private ClientInfoNoSqlDao clientInfoNoSqlDao;

    @Autowired
    private ClientMessageSendingNoSqlDao cmSendingNoSqlDao;

    @Autowired
    private ClientMessageWaitingNoSqlDao cmWaitingNoSqlDao;

    @Autowired
    private MessageInfoNoSqlDao messageInfoNoSqlDao;

    @Autowired
    private ChannelHolder channelHolder;
    /**
     * try to find client sending message and client waiting message to send when sdk online
     */
    public void findAvailableMessageToSend(String clientId){
        Long messageId = cmSendingNoSqlDao.get(clientId);
        if(messageId != null){
            WmpMessageProtos.WmpPublishMessageBody body = messageInfoNoSqlDao.getPublishMessageBody(messageId);
            if(body == null){
                return;
            }
            WmpPublishMessage msg = new WmpPublishMessage();
            msg.setVersion(Constants.WMP_VERSION);
            msg.setBody(body);
            channelHolder.getChannel(clientInfoNoSqlDao.getBrokerPrivateAddress(clientId)).
                    writeAndFlush(msg);
        }else{
            findWaitingMsgAndSend(clientId);
        }
    }

    /**
     * Clients are supposed to be online if it send back PUBACK message;
     * Find waitting messages and send them recursively, which means publish --> puback --> publish --> puback ...
     * until client message waiting list is empty.
     * @param clientId
     */
    public void findWaitingMsgAndSend(String clientId){
        Long messageId = cmWaitingNoSqlDao.dequeue(clientId);
        if(messageId == null) {
            return;
        }
        if(cmSendingNoSqlDao.setNotExist(clientId, messageId)){
            WmpPublishMessage publishMessage = new WmpPublishMessage();
            publishMessage.setVersion(Constants.WMP_VERSION);
            WmpMessageProtos.WmpPublishMessageBody body = messageInfoNoSqlDao.getPublishMessageBody(messageId);
            if(body == null){
                return;
            }
            publishMessage.setBody(body);
            channelHolder.getChannel(clientInfoNoSqlDao.getBrokerPrivateAddress(clientId)).
                    writeAndFlush(publishMessage);
        }else{
            cmWaitingNoSqlDao.enqueue(clientId, messageId);
        }
    }
}
