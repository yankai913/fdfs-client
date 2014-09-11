package com.zoo.fdfs;

import org.junit.Assert;
import org.junit.Test;

import com.zoo.fdfs.api.FdfsException;

public class DownloadClientTest extends BaseClientTest {

    @Test
    public void test_downloadFile_1() throws FdfsException {
        Assert.assertNotNull(downloadClient);
        String groupName = "group1";
        String remoteFileName = "M00/00/00/05vh0lQRV3iAAceIAAAAHocIkxU323.txt";
        byte[] arr = downloadClient.downloadFile(groupName, remoteFileName);
        Assert.assertNotNull(arr);
    }
    
    @Test
    public void test_downloadFile_2() throws FdfsException {
        Assert.assertNotNull(downloadClient);
        String groupName = "group1";
        String remoteFileName = "M00/00/00/05vh0lQRV3iAAceIAAAAHocIkxU323.txt";
        String localFileName = "d:/11.txt";
        int result = downloadClient.downloadFile(groupName, remoteFileName, localFileName);
        Assert.assertEquals(0, result);
    }
    
    @Test
    public void test_deleteFile() throws FdfsException {
        Assert.assertNotNull(downloadClient);
        String groupName = "group1";
        String remoteFileName = "M00/00/00/05vh0lQRVU6AD8CjAAAAHocIkxU005.txt";
        int result = downloadClient.deleteFile(groupName, remoteFileName);
        Assert.assertEquals(0, result);
    }
}
