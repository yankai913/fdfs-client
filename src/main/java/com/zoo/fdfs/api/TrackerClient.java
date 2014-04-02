package com.zoo.fdfs.api;

import java.util.Set;


/**
 * 
 * @author yankai913@gmail.com
 * @date 2014-3-13
 */
public interface TrackerClient {

    public Set<String> getTrackerServerAddrSet();


    public StorageConfig getStoreStorageOne(String groupName);


    public Set<StorageConfig> getStoreStorageSet(String groupName);


    public Set<StorageConfig> getFetchStorageSet(String groupName, String fileName);


    public GroupStat[] listGroups();


    public StorageStat[] listStorages(String groupName, String storageServerAddr);


    public StorageConfig getUpdateStorage(String groupName, String fileName);


    public boolean deleteStorage(TrackerGroup trackerGroup, String groupName, String storageServerAddr);


    public boolean deleteStorage(String groupName, String storageServerAddr);
}
