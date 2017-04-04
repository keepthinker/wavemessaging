package com.keepthinker.wavemessaging.proto;

import com.google.protobuf.InvalidProtocolBufferException;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import static com.keepthinker.wavemessaging.proto.WmpMessageProtos.*;

/**
 * Created by keepthinker on 2017/3/13.
 */
public class WmpDecoder extends ReplayingDecoder {

    private static final Logger LOGGER = LogManager.getLogger();
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        try {
            byte firstByte = in.readByte();
            int version = (firstByte & 0xF0) >> 4;
            int method = firstByte & 0x0F;
            WmpMessageMethod wmpMethod = WmpMessageMethod.toEnum(method);
            switch (wmpMethod) {
                case CONNECT:
                    out.add(decodeConnectMessage(in, version));
                    break;
                case CONNACK:
                    out.add(decodeConnAckMessage(in, version));
                    break;

                case PINGREQ:
                    out.add(new WmpPingReqMessage(version));
                    break;
                case PINGRESP:
                    out.add(new WmpPingRespMessage(version));
                    break;
                case DISCONNECT:
                    out.add(decodeDisConnect(in, version));
                    break;
                default:
                    LOGGER.error("method code unknow|{}", wmpMethod);
            }
        }catch(Exception e){
            LOGGER.error("unknow error in decoding message", e);
        }

    }

    private WmpDisConnectMessage decodeDisConnect(ByteBuf in, int version){
        WmpDisConnectMessage message = new WmpDisConnectMessage();
        message.setVersion(version);

        int bodySize = in.readInt();
        byte[] bytes = new byte[bodySize];
        in.readBytes(bytes);
        try {
            WmpDisConnectMessageBody body = WmpDisConnectMessageBody.parseFrom(bytes);
            message.setBody(body);
            return message;
        } catch (InvalidProtocolBufferException e) {
            LOGGER.error("error in decoding connect messsage body",  e);
            return null;
        }
    }

    private WmpConnectMessage decodeConnectMessage(ByteBuf in, int version){
        WmpConnectMessage message = new WmpConnectMessage();
        message.setVersion(version);

        int bodySize = in.readInt();
        byte[] bytes = new byte[bodySize];
        in.readBytes(bytes);
        try {
            WmpConnectMessageBody body = WmpConnectMessageBody.parseFrom(bytes);
            message.setBody(body);
            return message;
        } catch (InvalidProtocolBufferException e) {
            LOGGER.error("error in decoding connect messsage body",  e);
            return null;
        }

    }

    private WmpConnAckMessage decodeConnAckMessage(ByteBuf in, int version){
        WmpConnAckMessage message = new WmpConnAckMessage();
        message.setVersion(version);

        int bodySize = in.readInt();
        byte[] bytes = new byte[bodySize];
        in.readBytes(bytes);
        try {
            WmpConnAckMessageBody body = WmpConnAckMessageBody.parseFrom(bytes);
            message.setBody(body);
            return message;
        } catch (InvalidProtocolBufferException e) {
            LOGGER.error("error in decoding connack messsage body ",  e);
            return null;
        }
    }

    private WmpPublishMessage decodePublishMessage(ByteBuf in, int version){
        WmpPublishMessage message = new WmpPublishMessage();
        message.setVersion(version);

        int bodySize = in.readInt();
        byte[] bytes = new byte[bodySize];
        in.readBytes(bytes);
        try {
            WmpPublishMessageBody body = WmpPublishMessageBody.parseFrom(bytes);
            message.setBody(body);
            return message;
        }catch (InvalidProtocolBufferException e) {
            LOGGER.error("error in decoding publish messsage body",  e);
            return null;
        }

    }

}
