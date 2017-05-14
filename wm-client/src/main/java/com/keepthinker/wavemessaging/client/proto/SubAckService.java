package com.keepthinker.wavemessaging.client.proto;

import com.keepthinker.wavemessaging.proto.WmpSubAckMessage;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

/**
 * Created by keepthinker on 2017/5/14.
 */
@Service
public class SubAckService  implements ProtocolService<WmpSubAckMessage> {

    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void handle(ChannelHandlerContext ctx, WmpSubAckMessage msg) {
        LOGGER.info("receive SubAck reponse from server|version:{}|clientId:{}|subscribeId:{}", msg.getVersion(), msg.getBody().getClientId(), msg.getBody().getSubscribeId());

    }
}
