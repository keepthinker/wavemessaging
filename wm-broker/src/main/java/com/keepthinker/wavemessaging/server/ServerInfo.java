package com.keepthinker.wavemessaging.server;

/**
 * record the number of clients, handlers, total connections in zookeeper.
 *
 * @author keepthinker
 */
public class ServerInfo {
    private int numOfClientChannels;
    private int numOfHandlerChannels;
    private int totalNumOfChannels;

    public int getNumOfClientChannels() {
        return numOfClientChannels;
    }

    public void setNumOfClientChannels(int numOfClientChannels) {
        this.numOfClientChannels = numOfClientChannels;
    }

    public int getNumOfHandlerChannels() {
        return numOfHandlerChannels;
    }

    public void setNumOfHandlerChannels(int numOfHandlerChannels) {
        this.numOfHandlerChannels = numOfHandlerChannels;
    }

    public int getTotalNumOfChannels() {
        return totalNumOfChannels;
    }

    public void setTotalNumOfChannels(int totalNumOfChannels) {
        this.totalNumOfChannels = totalNumOfChannels;
    }


}
