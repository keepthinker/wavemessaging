package com.keepthinker.wavemessaging.server.proto;

import com.keepthinker.wavemessaging.core.ProtocolService;
import com.keepthinker.wavemessaging.core.utils.WmpUtils;
import com.keepthinker.wavemessaging.proto.WmpConnAckMessage;
import com.keepthinker.wavemessaging.proto.WmpMessageProtos;
import com.keepthinker.wavemessaging.server.SdkChannelManager;
import com.keepthinker.wavemessaging.server.model.ChannelInfo;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * handler Handle connack response to decide whether or not to keep or close connection from sdk.
 */
@Service
public class ConnAckService implements ProtocolService<WmpConnAckMessage> {

    @Resource
    private SdkChannelManager sdkChannelManager;

    @Override
    public void handle(ChannelHandlerContext ctx, WmpConnAckMessage msg) {
        Channel channel = sdkChannelManager.getChannel(msg.getBody().getClientId());
        if(msg.getBody().getReturnCode() == WmpMessageProtos.WmpConnectReturnCode.ACCEPTED) {
            ChannelInfo info = sdkChannelManager.getChannelInfo(channel);
            info.setAccessTime(new Date());
            channel.writeAndFlush(WmpUtils.CONNACK_ACCEPTED_MESSAGE);

        }else{
            if(msg.getBody().getReturnCode() == WmpMessageProtos.WmpConnectReturnCode.REFUSED_NOT_AUTHORIZED) {
                channel.writeAndFlush(WmpUtils.CONNACK_REFUSED_INVALID_CLIENT_ID_OR_TOKEN_MESSAGE);
            }else if(msg.getBody().getReturnCode() == WmpMessageProtos.WmpConnectReturnCode.REFUSED_IDENTIFIER_REJECTED){
                channel.writeAndFlush((WmpUtils.CONNECTION_REFUSED_CLIENT_ID_REJECTED_MESSAGE));
            }else{
                channel.writeAndFlush(WmpUtils.CONNACK_REFUSED_INVALID_CLIENT_ID_OR_TOKEN_MESSAGE);
            }
            sdkChannelManager.remove(msg.getBody().getClientId());
        }
    }

}
