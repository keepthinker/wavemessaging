package com.keepthinker.wavemessaging.handler.utils;

import com.keepthinker.wavemessaging.core.ChildrenChangeListener;
import com.keepthinker.wavemessaging.core.utils.Constants;
import com.keepthinker.wavemessaging.core.utils.SpringUtils;
import com.keepthinker.wavemessaging.core.utils.WmUtils;
import com.keepthinker.wavemessaging.core.utils.ZkCommonUtils;
import com.keepthinker.wavemessaging.handler.ChannelCreater;
import com.keepthinker.wavemessaging.handler.ChannelHolder;
import com.keepthinker.wavemessaging.proto.WmpConnectMessage;
import io.netty.channel.Channel;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ZkHanlderUtils {

    private static final Logger LOGGER = LogManager.getLogger();

    public static void watchZkBrokers() {
        final ChannelCreater creater = SpringUtils.getContext().getBean(ChannelCreater.class);
        final ChannelHolder holder = SpringUtils.getContext().getBean(ChannelHolder.class);
        ZkCommonUtils.watchChildren(Constants.ZK_BROKER_BASE_PATH, new ChildrenChangeListener() {
            @Override
            public void removed(PathChildrenCacheEvent event) {
                String serverAddr = ZkCommonUtils.get1LevelPath(event.getData().getPath());
                LOGGER.info("removed {}", serverAddr);
                String[] arr = serverAddr.split(":");
                String host = arr[0];
                int port = Integer.valueOf(arr[1]);
                holder.remove(host, port);
            }

            @Override
            public void added(PathChildrenCacheEvent event) {
                String serverAddr = ZkCommonUtils.get1LevelPath(event.getData().getPath());
                LOGGER.info("added {}", serverAddr);
                String[] arr = serverAddr.split(":");
                String host = arr[0];
                int port = Integer.valueOf(arr[1]);

                Channel channel = creater.connect(host, port);

                WmpConnectMessage connectMessage = HandlerUtils.HANDLER_CONNECT_MESSAGE;
                channel.writeAndFlush(connectMessage);
            }
        });
    }

    public static void registerReceiver(){
        boolean result = ZkCommonUtils.createIfNotExisted(Constants.ZK_HANDLER_BASE_PATH);
        if (result == false) {
            throw new RuntimeException("create a node in zookeeper failed");
        }

        ZkCommonUtils.createEphemeral(Constants.ZK_HANDLER_BASE_PATH
                        + Constants.SIGN_SLASH + WmUtils.getIPV4Private(),
                "");
    }


}
