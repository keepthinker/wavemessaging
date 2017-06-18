package com.keepthinker.wavemessaging.handler.proto;

import com.keepthinker.wavemessaging.core.ProtocolService;
import com.keepthinker.wavemessaging.handler.ChannelHolder;
import com.keepthinker.wavemessaging.nosql.ClientInfoNoSqlDao;
import com.keepthinker.wavemessaging.nosql.TopicNoSqlDao;
import com.keepthinker.wavemessaging.nosql.redis.RedisUtils;
import com.keepthinker.wavemessaging.proto.WmpMessageProtos;
import com.keepthinker.wavemessaging.proto.WmpUnsubAckMessage;
import com.keepthinker.wavemessaging.proto.WmpUnsubscribeMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by keepthinker on 2017/6/18.
 */
@Service
public class UnsubscribeService implements ProtocolService<WmpUnsubscribeMessage> {

    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    private TopicNoSqlDao topicNoSqlDao;

    @Autowired
    private ChannelHolder channelHolder;

    @Autowired
    private ClientInfoNoSqlDao clientInfoNoSqlDao;

    @Override
    public void handle(ChannelHandlerContext ctx, WmpUnsubscribeMessage msg) {
        WmpMessageProtos.WmpUnsubscribeMessageBody body = msg.getBody();
        switch(body.getTargetType()){
            case TOPIC_GENERAL:
                List<String> topics = body.getTopicsList();
                handleTopicGeneral(body.getClientId(), body.getUnsubscribeId(), topics);
                break;
            default:
                LOGGER.error("unrecognized target type|{}", body.getTargetType());
        }
    }

    private void handleTopicGeneral(String clientId, int unsubscribeId, List<String> topics){
        Long successNum = 0L;
        for(String topic : topics){
            String topicKey = RedisUtils.getTopicGeneralPrefix(topic, Long.valueOf(clientId));
            Long result = topicNoSqlDao.delete(topicKey, clientId);
            successNum += result != null ? result : 0L;
        }

        WmpMessageProtos.UnsubscribeReturnCode unsubscribeReturnCode;
        if(successNum >= topics.size()){
            unsubscribeReturnCode = WmpMessageProtos.UnsubscribeReturnCode.UNSUB_SUCCESS;
        }else{
            unsubscribeReturnCode = WmpMessageProtos.UnsubscribeReturnCode.UNSUB_FAILURE;
        }
        WmpUnsubAckMessage wmpUnsubAckMessage = new WmpUnsubAckMessage();
        WmpMessageProtos.WmpUnsubAckMessageBody body = WmpMessageProtos.WmpUnsubAckMessageBody.newBuilder()
                .setClientId(clientId)
                .setUnsubscribeId(unsubscribeId)
                .setReturnCode(unsubscribeReturnCode)
                .build();
        wmpUnsubAckMessage.setBody(body);

        String brokerPrivateAddress = clientInfoNoSqlDao.getBrokerPrivateAddress(clientId);
        Channel brokerChannel = channelHolder.getChannel(brokerPrivateAddress);
        brokerChannel.writeAndFlush(wmpUnsubAckMessage);

    }
}
