package com.keepthinker.wavemessaging.proto;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * Created by keepthinker on 2017/3/12.
 */
public class WmpEncoder extends MessageToMessageEncoder<WmpMessage> {
    @Override
    protected void encode(ChannelHandlerContext ctx, WmpMessage msg, List<Object> out) throws Exception {

    }
}
