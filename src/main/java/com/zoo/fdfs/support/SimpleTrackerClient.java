package com.zoo.fdfs.support;

import java.io.IOException;
import java.net.Socket;
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
import com.zoo.fdfs.common.ConcurrentHashSet;


/**
 * Prototype, Threadsafe
 * 
 * @author yankai913@gmail.com
 * @date 2014-3-14
 */
public class SimpleTrackerClient implements TrackerClient {

    private final Set<String> trackerServerAddrSet = new HashSet<String>();

    private final Set<String> storageServerAddrSet = new HashSet<String>();

    private final Map<String, Socket> addr2Socket = new HashMap<String, Socket>();
    
    public SimpleTrackerClient(FdfsClientConfigurable fdfsClientConfigurable) {
        String trackerServerAddr = fdfsClientConfigurable.getTrackerServerAddr();
        if (trackerServerAddr == null || trackerServerAddr.length() == 0) {
            throw new IllegalArgumentException("trackerServerAddr is blank");
        }
        String[] trackerServerAddrArr = trackerServerAddr.trim().split(",");
        for (String addr : trackerServerAddrArr) {
            trackerServerAddrSet.add(addr);
        }
        for (String addr : trackerServerAddrSet) {
            try {
                Socket socket = new Socket(addr.split(":")[0], Integer.parseInt(addr.split(":")[1]));
                //TODO set socket param
                
            }
            catch (Exception e) {
                throw new IllegalArgumentException(e);
            }
        }
    }


    @Override
    public Set<String> getTrackerServerAddrSet() {
        return trackerServerAddrSet;
    }


    @Override
    public Set<String> getStorageServerAddrSet() {
        return storageServerAddrSet;
    }


    @Override
    public List<StorageConfig> getStorageList(byte cmd, String groupName, String fileName) {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public StorageConfig getStoreStorageOne() {
        // TODO Auto-generated method stub
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
