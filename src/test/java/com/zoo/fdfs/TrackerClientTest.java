package com.zoo.fdfs;

import java.util.Set;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.zoo.fdfs.api.FdfsClientConfigurable;
import com.zoo.fdfs.api.StorageConfig;
import com.zoo.fdfs.api.TrackerClient;
import com.zoo.fdfs.support.SimpleTrackerClient;


public class TrackerClientTest {

    static TrackerClient trackerClient = null;


    @BeforeClass
    public static void execBefore() {
        String addr = "192.168.81.129:22122,192.168.81.129:22122";
        FdfsClientConfigurable fdfsClientConfigurable = new FdfsClientConfigurable(addr);
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
}
