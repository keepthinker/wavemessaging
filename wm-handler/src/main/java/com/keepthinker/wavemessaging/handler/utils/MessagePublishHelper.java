package com.keepthinker.wavemessaging.handler.utils;

import com.keepthinker.wavemessaging.core.utils.Constants;
import com.keepthinker.wavemessaging.handler.ChannelHolder;
import com.keepthinker.wavemessaging.nosql.ClientInfoNoSqlDao;
import com.keepthinker.wavemessaging.nosql.ClientMessageSendingNoSqlDao;
import com.keepthinker.wavemessaging.nosql.ClientMessageWaitingNoSqlDao;
import com.keepthinker.wavemessaging.nosql.MessageInfoNoSqlDao;
import com.keepthinker.wavemessaging.nosql.redis.model.MessageInfo;
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
            publish(clientId, messageId);
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
    private void findWaitingMsgAndSend(String clientId){
        Long messageId = cmWaitingNoSqlDao.dequeue(clientId);
        if(messageId == null) {
            return;
        }
        if(cmSendingNoSqlDao.setNotExist(clientId, messageId)){

            publish(clientId, messageId);

        }else{
            cmWaitingNoSqlDao.enqueue(clientId, messageId);
        }
    }

    private void publish(String clientId, long messageId){
        MessageInfo messageInfo = messageInfoNoSqlDao.getPartialForPublish(messageId);

        if(messageInfo == null){
            cmSendingNoSqlDao.delete(clientId);
            findWaitingMsgAndSend(clientId);  //--todo should limit the number of recursion in case of bugs
            return;
        }

        WmpMessageProtos.WmpPublishMessageBody body = WmpMessageProtos.WmpPublishMessageBody.newBuilder()
                .setTargetTypeValue(messageInfo.getTargetType())
                .setTarget(messageInfo.getTarget())
                .setContent(messageInfo.getContent())
                .setTargetClientId(clientId)
                .setMessageId(messageId)
                .setDirection(WmpMessageProtos.Direction.TO_CLIENT_SDK)
                .build();

        WmpPublishMessage msg = new WmpPublishMessage();
        msg.setVersion(Constants.WMP_VERSION);
        msg.setBody(body);

        channelHolder.getChannel(clientInfoNoSqlDao.getBrokerPrivateAddress(clientId)).
                writeAndFlush(msg);

    }
}
