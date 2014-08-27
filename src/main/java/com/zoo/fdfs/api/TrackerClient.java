package com.zoo.fdfs.api;

import java.util.Set;


/**
 * 
 * @author yankai913@gmail.com
 * @date 2014-3-13
 */
public interface TrackerClient extends Constants {

    /**
     * get trackerServerAddrIp set
     * 
     * @return trackerServerIpSet
     * @throws FdfsException
     */
    public Set<String> getTrackerServerAddrSet() throws FdfsException;


    /**
     * get one storageServerConfig by groupName
     * 
     * @param groupName
     *            the group name to upload file, can be empty
     * @return StorageConfig
     * @throws FdfsException
     */
    public StorageConfig getStoreStorageOne(String groupName) throws FdfsException;


    /**
     * get storageServerConfig set by groupName
     * 
     * @param groupName
     *            the group name to upload file, can be empty
     * @return StorageConfigSet
     * @throws FdfsException
     */
    public Set<StorageConfig> getStoreStorageSet(String groupName) throws FdfsException;


    /**
     * 
     * @param groupName
     *            the group name of storage server
     * @param fileName
     *            filename on storage server
     * @return
     * @throws FdfsException
     */
    public Set<StorageConfig> getFetchStorageSet(String groupName, String fileName) throws FdfsException;


    /**
     * list groups
     * 
     * @return GroupStat Array
     * @throws FdfsException
     */
    public GroupStat[] listGroups() throws FdfsException;


    /**
     * query storage server stat info of the group
     * 
     * @param groupName
     *            the group name of storage server
     * @param storageServerAddr
     *            the storage server IP
     * @return StorageStat Array
     * @throws FdfsException
     */
    public StorageStat[] listStorages(String groupName, String storageServerAddr) throws FdfsException;


    /**
     * query storage server to update file (delete file or set meta data)
     * 
     * @param groupName
     *            the group name of storage server
     * @param fileName
     *            filename on storage server
     * @return StorageConfig
     * @throws FdfsException
     */
    public StorageConfig getUpdateStorage(String groupName, String fileName) throws FdfsException;


    /**
     * delete a storage server from the global FastDFS cluster
     * 
     * @param trackerGroup
     *            the tracker server group
     * @param groupName
     *            the group name of storage server
     * @param storageServerAddr
     *            the storage server IP address
     * @return true for success, false for fail
     * @throws FdfsException
     */
    public boolean deleteStorage(TrackerGroup trackerGroup, String groupName, String storageServerAddr)
            throws FdfsException;


    /**
     * delete a storage server from the global FastDFS cluster
     * 
     * @param groupName
     *            the group name of storage server
     * @param storageServerAddr
     *            the storage server IP address
     * @return
     * @throws FdfsException
     */
    public boolean deleteStorage(String groupName, String storageServerAddr) throws FdfsException;
}
