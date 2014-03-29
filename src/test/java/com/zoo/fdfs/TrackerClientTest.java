package com.zoo.fdfs;

import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.zoo.fdfs.api.FdfsClientConfigurable;
import com.zoo.fdfs.api.StorageConfig;
import com.zoo.fdfs.api.TrackerClient;
import com.zoo.fdfs.support.SimpleTrackerClient;


public class TrackerClientTest {

    @Test
    public void testGetStoreStorageOne() {
        String addr = "211.155.227.30:22122,211.155.227.30:22122";
        FdfsClientConfigurable fdfsClientConfigurable = new FdfsClientConfigurable(addr);
        TrackerClient trackerClient = new SimpleTrackerClient(fdfsClientConfigurable);
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

}
