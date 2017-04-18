package com.keepthinker.wavemessaging.client.proto;

import com.keepthinker.wavemessaging.proto.WmpMessage;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by keepthinker on 2017/4/9.
 */
public class PublishService implements ProtocolService<WmpMessage> {
    @Override
    public void handle(ChannelHandlerContext ctx, WmpMessage msg) {

    }
}
