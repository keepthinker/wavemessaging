package com.keepthinker.wavemessaging.proto;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * Created by keepthinker on 2017/3/12.
 */

@ChannelHandler.Sharable
public class WmpEncoder extends MessageToMessageEncoder<WmpMessage> {

    public static final WmpEncoder INSTANCE = new WmpEncoder();

    @Override
    protected void encode(ChannelHandlerContext ctx, WmpMessage msg, List<Object> out) throws Exception {
        ByteBufAllocator allocator = ctx.alloc();
        if (msg instanceof WmpConnectMessage) {
            out.add(encodeConnecMessage(allocator, (WmpConnectMessage) msg));
        } else if (msg instanceof WmpConnAckMessage) {
            out.add(encodeConnackMessage(allocator, (WmpConnAckMessage) msg));
        } else if (msg instanceof WmpPingReqMessage) {
            out.add(encodePingReqMessage(allocator, (WmpPingReqMessage) msg));
        } else if (msg instanceof WmpPingRespMessage) {
            out.add(encodePingRespMessage(allocator, (WmpPingRespMessage) msg));
        }
    }

    private ByteBuf encodeConnecMessage(ByteBufAllocator byteBufAllocator, WmpConnectMessage msg) {
        int methodSize = 1;
        WmpMessageProtos.WmpConnectMessageBody wmpConnectMessage = msg.getBody();
        int bodySize = wmpConnectMessage.getSerializedSize();
        ByteBuf byteBuffer = byteBufAllocator.buffer(methodSize + bodySize);
        byteBuffer.writeByte(msg.getMethod().getCode() | msg.getVersion() << 4);
        byteBuffer.writeInt(bodySize);
        byteBuffer.writeBytes(wmpConnectMessage.toByteArray());
        return byteBuffer;
    }

    private ByteBuf encodeConnackMessage(ByteBufAllocator byteBufAllocator, WmpConnAckMessage msg) {
        int methodSize = 1;
        WmpMessageProtos.WmpConnAckMessageBody wmpConnackMessage = msg.getBody();
        int bodySize = wmpConnackMessage.getSerializedSize();
        ByteBuf byteBuffer = byteBufAllocator.buffer(methodSize + bodySize);
        byteBuffer.writeByte(msg.getMethod().getCode() | msg.getVersion() << 4);
        byteBuffer.writeInt(bodySize);
        byteBuffer.writeBytes(wmpConnackMessage.toByteArray());
        return byteBuffer;
    }

    private ByteBuf encodePingReqMessage(ByteBufAllocator byteBufAllocator, WmpPingReqMessage msg) {
        int methodSize = 1;
        ByteBuf byteBuffer = byteBufAllocator.buffer(methodSize);
        byteBuffer.writeByte(msg.getMethod().getCode() | msg.getVersion() << 4);
        return byteBuffer;
    }

    private ByteBuf encodePingRespMessage(ByteBufAllocator byteBufAllocator, WmpPingRespMessage msg) {
        int methodSize = 1;
        ByteBuf byteBuffer = byteBufAllocator.buffer(methodSize);
        byteBuffer.writeByte(msg.getMethod().getCode() | msg.getVersion() << 4);
        return byteBuffer;
    }
}
