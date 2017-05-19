package com.keepthinker.wavemessaging.core;

/**
 * Created by keepthinker on 2017/5/19.
 */
public interface ThreadRejectedListener {
    void abortEvent(Runnable runnable);
}
