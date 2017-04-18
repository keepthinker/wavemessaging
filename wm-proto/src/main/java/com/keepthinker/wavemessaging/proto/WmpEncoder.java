package com.keepthinker.wavemessaging.proto;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

import static com.keepthinker.wavemessaging.proto.WmpMessageProtos.*;

/**
 * Created by keepthinker on 2017/3/12.
 */

@ChannelHandler.Sharable
public class WmpEncoder extends MessageToMessageEncoder<WmpMessage> {

    public static final WmpEncoder INSTANCE = new WmpEncoder();

    /** the byte size method field occupied */
    private static final int METHOD_SIZE = 1;

    @Override
    protected void encode(ChannelHandlerContext ctx, WmpMessage msg, List<Object> out) throws Exception {
        ByteBufAllocator allocator = ctx.alloc();
        if (msg instanceof WmpConnectMessage) {
            out.add(encodeConnecMessage(allocator, (WmpConnectMessage)msg));
        } else if (msg instanceof WmpConnAckMessage) {
            out.add(encodeConnAckMessage(allocator, (WmpConnAckMessage)msg));
        } else if (msg instanceof WmpDisConnectMessage){
            out.add(encodeDisConnectMessage(allocator, (WmpDisConnectMessage)msg));
        }  else if (msg instanceof WmpPingReqMessage) {
            out.add(encodePingReqMessage(allocator, (WmpPingReqMessage)msg));
        } else if (msg instanceof WmpPingRespMessage) {
            out.add(encodePingRespMessage(allocator, (WmpPingRespMessage)msg));
        } else if (msg instanceof  WmpPublishMessage) {
            out.add(endodePublishMessage(allocator, (WmpPublishMessage)msg));
        }else if (msg instanceof  WmpPubAckMessage) {
            out.add(endodePubAckMessage(allocator, (WmpPubAckMessage) msg));
        }
    }

    private ByteBuf encodeConnecMessage(ByteBufAllocator byteBufAllocator, WmpConnectMessage msg) {
        WmpConnectMessageBody body = msg.getBody();
        int bodySize = body.getSerializedSize();
        ByteBuf byteBuffer = byteBufAllocator.buffer(METHOD_SIZE + bodySize);
        byteBuffer.writeByte(msg.getMethod().getCode() | msg.getVersion() << 4);
        byteBuffer.writeInt(bodySize);
        byteBuffer.writeBytes(body.toByteArray());
        return byteBuffer;
    }

    private ByteBuf encodeConnAckMessage(ByteBufAllocator byteBufAllocator, WmpConnAckMessage msg) {
        WmpConnAckMessageBody body = msg.getBody();
        int bodySize = body.getSerializedSize();
        ByteBuf byteBuffer = byteBufAllocator.buffer(METHOD_SIZE + bodySize);
        byteBuffer.writeByte(msg.getMethod().getCode() | msg.getVersion() << 4);
        byteBuffer.writeInt(bodySize);
        byteBuffer.writeBytes(body.toByteArray());
        return byteBuffer;
    }

    private ByteBuf encodeDisConnectMessage(ByteBufAllocator byteBufAllocator, WmpDisConnectMessage msg){
        WmpDisConnectMessageBody body = msg.getBody();
        int bodySize = body.getSerializedSize();
        ByteBuf byteBuf = byteBufAllocator.buffer(METHOD_SIZE + bodySize);
        byteBuf.writeByte(msg.getMethod().getCode() | msg.getVersion() << 4);
        byteBuf.writeInt(bodySize);
        byteBuf.writeBytes(body.toByteArray());
        return byteBuf;
    }

    private ByteBuf encodePingReqMessage(ByteBufAllocator byteBufAllocator, WmpPingReqMessage msg) {
        ByteBuf byteBuffer = byteBufAllocator.buffer(METHOD_SIZE);
        byteBuffer.writeByte(msg.getMethod().getCode() | msg.getVersion() << 4);
        return byteBuffer;
    }

    private ByteBuf encodePingRespMessage(ByteBufAllocator byteBufAllocator, WmpPingRespMessage msg) {
        ByteBuf byteBuffer = byteBufAllocator.buffer(METHOD_SIZE);
        byteBuffer.writeByte(msg.getMethod().getCode() | msg.getVersion() << 4);
        return byteBuffer;
    }

    private ByteBuf endodePublishMessage(ByteBufAllocator byteBufAllocator, WmpPublishMessage msg){
        WmpPublishMessageBody body = msg.getBody();
        int bodySize = body.getSerializedSize();
        ByteBuf byteBuffer = byteBufAllocator.buffer(METHOD_SIZE + bodySize);
        byteBuffer.writeByte(msg.getMethod().getCode() | msg.getVersion() << 4);
        byteBuffer.writeInt(bodySize);
        byteBuffer.writeBytes(body.toByteArray());
        return byteBuffer;
    }

    private ByteBuf endodePubAckMessage(ByteBufAllocator byteBufAllocator, WmpPubAckMessage msg){
        WmpPubAckMessageBody body = msg.getBody();
        int bodySize = body.getSerializedSize();
        ByteBuf byteBuffer = byteBufAllocator.buffer(METHOD_SIZE + bodySize);
        byteBuffer.writeByte(msg.getMethod().getCode() | msg.getVersion() << 4);
        byteBuffer.writeInt(bodySize);
        byteBuffer.writeBytes(body.toByteArray());
        return byteBuffer;
    }
}
