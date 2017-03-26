package com.keepthinker.wavemessaging.client.proto;

import com.keepthinker.wavemessaging.proto.WmpConnAckMessage;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

/**
 * Created by keepthinker on 2017/3/26.
 */
@Service
public class ConnAckService implements ProtocolService<WmpConnAckMessage> {

    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void handle(ChannelHandlerContext ctx, WmpConnAckMessage msg) {
        LOGGER.info("receive ConnAck reponse from server|{}|{}", msg.getVersion(), msg.getBody());
    }

}
