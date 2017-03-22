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
            int method = in.readByte();
            WmpMessageMethod wmpMethod = WmpMessageMethod.toEnum(method);
            switch (wmpMethod) {
                case CONNECT:
                    out.add(decodeConnectMessage(in, wmpMethod));
                    break;
                case CONNACK:
                    out.add(decodeConnAckMessage(in, wmpMethod));
                    break;

                case PINGREQ:
                    out.add(new WmpPingReqMessage());
                    break;
                case PINGRESP:
                    out.add(new WmpPingRespMessage());
                default:
                    LOGGER.error("method code unknow|{}", wmpMethod);
            }
        }catch(Exception e){
            LOGGER.error("unknow error in decoding message", e);
        }

    }

    private WmpConnectMessage decodeConnectMessage(ByteBuf in, WmpMessageMethod wmpMethod){
        WmpConnectMessage message = new WmpConnectMessage();
        message.setMethod(wmpMethod);
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

    private WmpConnAckMessage decodeConnAckMessage(ByteBuf in, WmpMessageMethod wmpMethod){
        WmpConnAckMessage message = new WmpConnAckMessage();
        message.setMethod(wmpMethod);
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
