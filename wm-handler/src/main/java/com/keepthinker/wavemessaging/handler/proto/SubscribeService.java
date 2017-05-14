package com.keepthinker.wavemessaging.handler.proto;

import com.keepthinker.wavemessaging.core.ProtocolService;
import com.keepthinker.wavemessaging.handler.ChannelHolder;
import com.keepthinker.wavemessaging.nosql.ClientInfoNoSqlDao;
import com.keepthinker.wavemessaging.nosql.TopicNoSqlDao;
import com.keepthinker.wavemessaging.nosql.redis.RedisUtils;
import com.keepthinker.wavemessaging.proto.WmpMessageProtos;
import com.keepthinker.wavemessaging.proto.WmpSubAckMessage;
import com.keepthinker.wavemessaging.proto.WmpSubscribeMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Created by keepthinker on 2017/5/6.
 */
@Service
public class SubscribeService implements ProtocolService<WmpSubscribeMessage>{

    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    private TopicNoSqlDao topicNoSqlDao;

    @Autowired
    private ChannelHolder channelHolder;

    @Autowired
    private ClientInfoNoSqlDao clientInfoNoSqlDao;

    @Override
    public void handle(ChannelHandlerContext ctx, WmpSubscribeMessage msg) {
        WmpMessageProtos.WmpSubscribeMessageBody body = msg.getBody();
        switch(body.getTargetType()){
            case TOPIC_GENERAL:
                List<String> topics = body.getTopicsList();
                handleTopicGeneral(body.getClientId(), body.getSubscribeId(), topics);
                break;
            default:
                LOGGER.error("unrecognized save target type|{}", body.getTargetType());
        }
    }

    private void handleTopicGeneral(String clientId, int subscribeId, List<String> topics){
        Long successNum = 0L;
        for(String topic : topics){
            String topicKey = RedisUtils.getTopicGeneralPrefix(topic, Long.valueOf(clientId));
            Long result = topicNoSqlDao.save(topicKey, clientId);
            successNum += result != null ? result : 0L;
        }

        WmpMessageProtos.SubscribeReturnCode subscribeReturnCode;
        if(successNum >= topics.size()){
            subscribeReturnCode = WmpMessageProtos.SubscribeReturnCode.SUCCESS;
        }else{
            subscribeReturnCode = WmpMessageProtos.SubscribeReturnCode.FAILURE;
        }
        WmpSubAckMessage wmpSubAckMessage = new WmpSubAckMessage();
        WmpMessageProtos.WmpSubAckMessageBody body = WmpMessageProtos.WmpSubAckMessageBody.newBuilder()
                .setClientId(clientId)
                .setSubscribeId(subscribeId)
                .setReturnCode(subscribeReturnCode)
                .build();
        wmpSubAckMessage.setBody(body);

        String brokerPrivateAddress = clientInfoNoSqlDao.getBrokerPrivateAddress(clientId);
        Channel brokerChannel = channelHolder.getChannel(brokerPrivateAddress);
        brokerChannel.writeAndFlush(wmpSubAckMessage);

    }
}
