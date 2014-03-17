package com.zoo.fdfs.api;

import java.net.InetSocketAddress;


/**
 * 
 * @author yankai913@gmail.com
 * @date 2014-3-14
 */
public class StorageConfig {

    private int storePathIndex;

    private InetSocketAddress inetSocketAddress;


    public int getStorePathIndex() {
        return storePathIndex;
    }


    public void setStorePathIndex(int storePathIndex) {
        this.storePathIndex = storePathIndex;
    }


    public InetSocketAddress getInetSocketAddress() {
        return inetSocketAddress;
    }


    public void setInetSocketAddress(InetSocketAddress inetSocketAddress) {
        this.inetSocketAddress = inetSocketAddress;
    }


    @Override
    public String toString() {
        return "host:" + inetSocketAddress.getHostName() + ", port:"
                + inetSocketAddress.getPort() + ", storePathIndex:" + storePathIndex;
    }
}
