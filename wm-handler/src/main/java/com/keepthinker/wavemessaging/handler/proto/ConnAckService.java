package com.keepthinker.wavemessaging.handler.proto;

import com.keepthinker.wavemessaging.core.ProtocolService;
import com.keepthinker.wavemessaging.handler.ChannelHolder;
import com.keepthinker.wavemessaging.handler.utils.ZkHanlderUtils;
import com.keepthinker.wavemessaging.proto.WmpConnAckMessage;
import com.keepthinker.wavemessaging.proto.WmpMessageProtos;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * handler response for connect request from handlers to brokers
 * Created by keepthinker on 2017/1/27.
 */
@Service
public class ConnAckService implements ProtocolService<WmpConnAckMessage> {
    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    private ChannelHolder channelHolder;

    @Override
    public void handle(ChannelHandlerContext ctx, WmpConnAckMessage msg) {
        if(msg.getBody().getReturnCode() == WmpMessageProtos.ConnectReturnCode.ACCEPTED) {
            ZkHanlderUtils.registerReceiver();
            channelHolder.add(ctx.channel());
            LOGGER.info("connect to broker successfully|{}|{}|{}", msg.getMethod(), msg.getVersion(), msg.getBody());
        }else{
            LOGGER.error("can't connect to broker|{}|{}|{}", msg.getMethod(), msg.getVersion(), msg.getBody());
        }
    }
}
