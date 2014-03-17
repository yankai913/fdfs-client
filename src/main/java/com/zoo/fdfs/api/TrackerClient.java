package com.zoo.fdfs.api;

import java.util.List;
import java.util.Set;


/**
 * 
 * @author yankai913@gmail.com
 * @date 2014-3-13
 */
public interface TrackerClient {

    public Set<String> getTrackerServerAddrSet();


    public Set<String> getStorageServerAddrSet();


    List<StorageConfig> getStorageList(byte cmd, String groupName, String fileName);


    public StorageConfig getStoreStorageOne();


    public StorageConfig getStoreStorageOne(String groupName);


    public List<StorageConfig> getStoreStorageList(String groupName);


    public StorageConfig getFetchStorageOne(String groupName, String fileName);


    public List<StorageConfig> getFetchStorageList(String groupName, String fileName);


    public GroupStat[] listGroups();


    public StorageStat[] listStorages(String groupName, String storageServerAddr);


    public StorageStat[] listStorages(String groupName);
}
