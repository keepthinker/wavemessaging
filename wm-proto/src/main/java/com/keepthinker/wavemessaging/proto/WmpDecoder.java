package com.keepthinker.wavemessaging.proto;

import com.google.protobuf.InvalidProtocolBufferException;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

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
                default:
                    LOGGER.error("method code unknow|{}", wmpMethod);
            }
        }catch(Exception e){
            LOGGER.error("unknow error in decoding message", e);
        }

    }

    private WmpConnectMessage decodeConnectMessage(ByteBuf in, int version){
        WmpConnectMessage message = new WmpConnectMessage();
        message.setVersion(version);

        int bodySize = in.readInt();
        byte[] bytes = new byte[bodySize];
        in.readBytes(bytes);
        try {
            WmpMessageProtos.WmpConnectMessageBody body = WmpMessageProtos.WmpConnectMessageBody.parseFrom(bytes);
            message.setBody(body);
            return message;
        } catch (InvalidProtocolBufferException e) {
            LOGGER.error("error in decoding connect messsage ",  e);
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
            WmpMessageProtos.WmpConnAckMessageBody body = WmpMessageProtos.WmpConnAckMessageBody.parseFrom(bytes);
            message.setBody(body);
            return message;
        } catch (InvalidProtocolBufferException e) {
            LOGGER.error("error in decoding connack messsage ",  e);
            return null;
        }
    }

}
