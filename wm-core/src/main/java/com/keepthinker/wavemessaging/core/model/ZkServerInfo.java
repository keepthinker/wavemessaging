package com.keepthinker.wavemessaging.core.model;

public class ZkServerInfo {
    private int numOfSdkChannels;
    private int numOfHandlerChannels;

    public int getNumOfSdkChannels() {
        return numOfSdkChannels;
    }

    public void setNumOfSdkChannels(int numOfSdkChannels) {
        this.numOfSdkChannels = numOfSdkChannels;
    }

    public int getNumOfHandlerChannels() {
        return numOfHandlerChannels;
    }

    public void setNumOfHandlerChannels(int numOfHandlerChannels) {
        this.numOfHandlerChannels = numOfHandlerChannels;
    }

    public int getTotalNumOfChannels() {
        return numOfSdkChannels + numOfHandlerChannels;
    }

}
