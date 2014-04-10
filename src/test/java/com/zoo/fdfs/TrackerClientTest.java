package com.zoo.fdfs;

import java.util.Set;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.zoo.fdfs.api.FdfsClientConfig;
import com.zoo.fdfs.api.GroupStat;
import com.zoo.fdfs.api.StorageConfig;
import com.zoo.fdfs.api.StorageStat;
import com.zoo.fdfs.api.TrackerClient;
import com.zoo.fdfs.support.SimpleTrackerClient;


/**
 * 
 * @author yankai913@gmail.com
 * @date 2014-4-2
 */
public class TrackerClientTest {

    static TrackerClient trackerClient = null;


    @BeforeClass
    public static void execBefore() {
        String addr = "211.155.225.210:22122,211.155.225.210:22122";
        FdfsClientConfig fdfsClientConfigurable = new FdfsClientConfig(addr);
        trackerClient = new SimpleTrackerClient(fdfsClientConfigurable);
    }


    @Test
    public void testGetStoreStorageOne() {
        StorageConfig storageConfig = trackerClient.getStoreStorageOne(null);
        System.out.println(storageConfig);
        Assert.assertNotNull(storageConfig);
        storageConfig = trackerClient.getStoreStorageOne("group1");
        System.out.println(storageConfig);
        Assert.assertNotNull(storageConfig);
        Set<StorageConfig> set = trackerClient.getStoreStorageSet(null);
        System.out.println(set);
        Assert.assertNotNull(set);
        set = trackerClient.getStoreStorageSet("group1");
        System.out.println(set);
        Assert.assertNotNull(set);
    }


    @Test
    public void testGetFetchStorageSet() {
        String groupName = "group1";
        String fileName = "M00/00/00/wKhRgVM4TPSAd9jZAACGpvjJe5c81404_big.h";
        Set<StorageConfig> set = trackerClient.getFetchStorageSet(groupName, fileName);
        System.out.println(set);
        Assert.assertNotNull(set);
    }


    @Test
    public void testListGroups() {
        GroupStat[] groupStatArr = trackerClient.listGroups();
        Assert.assertNotNull(groupStatArr);
        for (GroupStat gs : groupStatArr) {
            System.out.println(gs);
        }
    }


    @Test
    public void testListStorages() {
        String groupName = "group1";
        String storageServerAddr = "211.155.225.210:23000";
        StorageStat[] storageStatArr = trackerClient.listStorages(groupName, storageServerAddr);
        Assert.assertNotNull(storageStatArr);
        for (StorageStat ss : storageStatArr) {
            System.out.println(ss);
        }
    }

    @Test
    public void testDeleteStorage() {
        //TODO 暂时先不删除，最后做这个测试。
        
    }
}
