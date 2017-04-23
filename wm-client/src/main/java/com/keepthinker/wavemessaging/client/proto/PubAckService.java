package com.keepthinker.wavemessaging.client.proto;

import com.keepthinker.wavemessaging.proto.WmpPubAckMessage;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Service;

/**
 * Created by keepthinker on 2017/4/21.
 */
@Service
public class PubAckService implements ProtocolService<WmpPubAckMessage>  {

    @Override
    public void handle(ChannelHandlerContext ctx, WmpPubAckMessage msg) {

    }
}
