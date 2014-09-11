package com.zoo.fdfs;

import com.zoo.fdfs.api.FdfsClientConfig;
import com.zoo.fdfs.api.TrackerClient;
import com.zoo.fdfs.support.SimpleTrackerClient;
import com.zoo.fdfs.support.client.impl.DefaultDownloadClient;
import com.zoo.fdfs.support.client.impl.DefaultUploadClient;

public class BaseClientTest {

    static TrackerClient trackerClient = null;
    static DefaultUploadClient uploadClient = null;
    static String localFileName = null;
    static FdfsClientConfig fdfsClientConfig = null;
    static DefaultDownloadClient downloadClient = null;
    
    static {
        String addr = "211.155.225.210:22122,211.155.225.210:22122";
        fdfsClientConfig = new FdfsClientConfig(addr);
        trackerClient = new SimpleTrackerClient(fdfsClientConfig);
        uploadClient = new DefaultUploadClient(fdfsClientConfig, trackerClient);
        localFileName = UploadClientTest.class.getResource("test-data.txt").toString().replace("file:/", "");
        System.out.println(localFileName);
        downloadClient = new DefaultDownloadClient(fdfsClientConfig, trackerClient);
    }
}
