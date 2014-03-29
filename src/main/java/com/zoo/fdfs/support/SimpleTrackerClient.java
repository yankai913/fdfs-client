package com.zoo.fdfs.support;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.zoo.fdfs.api.FdfsClientConfigurable;
import com.zoo.fdfs.api.GroupStat;
import com.zoo.fdfs.api.StorageConfig;
import com.zoo.fdfs.api.StorageStat;
import com.zoo.fdfs.api.TrackerClient;
import com.zoo.fdfs.api.TrackerGroup;
import com.zoo.fdfs.common.ConcurrentHashSet;
import com.zoo.fdfs.common.Strings;


/**
 * Prototype, Threadsafe
 * 
 * @author yankai913@gmail.com
 * @date 2014-3-14
 */
public class SimpleTrackerClient implements TrackerClient {

    private final Set<StorageConfig> storageServerAddrSet = new HashSet<StorageConfig>();

    private FdfsClientConfigurable fdfsClientConfigurable;

    private TrackerGroup trackerGroup;
    
    public SimpleTrackerClient(FdfsClientConfigurable fdfsClientConfigurable) {
        this.fdfsClientConfigurable = fdfsClientConfigurable;
        this.trackerGroup = new TrackerGroup(this.fdfsClientConfigurable);
    }

    
    @Override
    public Set<String> getTrackerServerAddrSet() {
        return this.trackerGroup.getAvailableTrackerServerAddrSet();
    }

    private List<StorageConfig> getStorageList(byte cmd, String groupName, String fileName) {
        Set<Socket> socketSet = this.trackerGroup.getGroupSocket();
        
        return null;
    }


    @Override
    public StorageConfig getStoreStorageOne() {
        return null;
    }


    @Override
    public StorageConfig getStoreStorageOne(String groupName) {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public List<StorageConfig> getStoreStorageList(String groupName) {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public StorageConfig getFetchStorageOne(String groupName, String fileName) {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public List<StorageConfig> getFetchStorageList(String groupName, String fileName) {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public GroupStat[] listGroups() {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public StorageStat[] listStorages(String groupName, String storageServerAddr) {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public StorageStat[] listStorages(String groupName) {
        // TODO Auto-generated method stub
        return null;
    }

}
