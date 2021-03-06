package com.keepthinker.wavemessaging.server;

import com.keepthinker.wavemessaging.core.utils.WmUtils;
import io.netty.channel.Channel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * thread safe class
 */
@Service
public class HandlerChannelMananger {
    private final List<Channel> HANDLER_CHANNELS = new ArrayList<>();

    private ReadWriteLock lock = new ReentrantReadWriteLock();
    Lock r = lock.readLock();
    Lock w = lock.writeLock();

    public void add(Channel handlerChannel) {
        w.lock();
        try {
            HANDLER_CHANNELS.add(handlerChannel);
        } finally {
            w.unlock();
        }
    }

    public int clearInvalid(){
        w.lock();
        try {
            return WmUtils.clearInvalidChannel(HANDLER_CHANNELS);
        } finally {
            w.unlock();
        }
    }


    public int size() {
        return HANDLER_CHANNELS.size();
    }

    public Channel get(String clientId) {
        r.lock();
        try {
            int size = HANDLER_CHANNELS.size();
            if(size == 0){
                return null;
            }
            int hashValue = hash(clientId);
            return HANDLER_CHANNELS.get(hashValue % size);
        } finally {
            r.unlock();
        }
    }

    public boolean remove(Channel channel){
        w.lock();
        try{
            return HANDLER_CHANNELS.remove(channel);
        } finally {
            w.unlock();
        }
    }

    private int hash(String str) {
        char[] value = str.toCharArray();
        int hash = 0;
        int h = hash;
        if (h == 0 && value.length > 0) {
            char val[] = value;

            for (int i = 0; i < value.length; i++) {
                h = 31 * h + val[i];
            }
            hash = h;
        }
        return h;
    }
}
