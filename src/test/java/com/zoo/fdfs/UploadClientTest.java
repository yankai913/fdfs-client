package com.zoo.fdfs;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.zoo.fdfs.api.Constants;
import com.zoo.fdfs.api.FdfsClientConfig;
import com.zoo.fdfs.api.FdfsException;
import com.zoo.fdfs.api.TrackerClient;
import com.zoo.fdfs.support.SimpleTrackerClient;
import com.zoo.fdfs.support.client.UploadClient;
import com.zoo.fdfs.support.client.impl.AbstractClient;
import com.zoo.fdfs.support.client.impl.DefaultUploadClient;


public class UploadClientTest extends BaseClientTest{


    @Test
    public void test_uploadFile_test() throws FdfsException, Exception {

    }


    @Test
    public void test_uploadFile_1() throws FdfsException {
        Assert.assertNotNull(uploadClient);
        Map<String, String> meta = null;
        String[] result = uploadClient.uploadFile(localFileName, "txt", meta);
        System.out.println(result[0]);
        System.out.println(result[1]);
        Assert.assertNotNull(result);
        /**
         * group1 M00/00/00/05vh0lQRV3iAAceIAAAAHocIkxU323.txt
         */
    }


    @Test
    public void test_setAndGetMeta() throws FdfsException {
        Map<String, String> meta = new HashMap<String, String>();
        meta.put("test", "testMeta");
        String groupName = "group1";
        String remoteFileName = "M00/00/00/05vh0lQRV3iAAceIAAAAHocIkxU323.txt";
        byte opFlag = Constants.STORAGE_SET_METADATA_FLAG_OVERWRITE;
        int result = uploadClient.setMetadata(groupName, remoteFileName, meta, opFlag);
        Assert.assertEquals(0, result);
        Map<String, String> map = uploadClient.getMetadata(groupName, remoteFileName);
        Assert.assertNotNull(map);
        System.out.println(map);
    }


    @Test
    public void test_uploadAppendFile_1() throws FdfsException, Exception {
        byte[] fileBuff = "5ffff".getBytes(fdfsClientConfig.getCharset());
        Map<String, String> meta = null;
        String[] result = uploadClient.uploadAppenderFile(fileBuff, "txt", meta);
        System.out.println(result[0]);
        System.out.println(result[1]);
        Assert.assertNotNull(result);
    }


    @Test
    public void test_uploadAppendFile_2() throws FdfsException, Exception {
        byte[] fileBuff = "5ffff".getBytes(fdfsClientConfig.getCharset());
        Map<String, String> meta = null;
        String groupName = "group1";
        String[] result = uploadClient.uploadAppenderFile(groupName, fileBuff, "txt", meta);
        System.out.println(result[0]);
        System.out.println(result[1]);
        Assert.assertNotNull(result);
    }


    @Test
    public void test_appendFile_1() throws FdfsException, Exception {
        String groupName = "group1";
        String appenderFileName = "M00/00/00/05vh0lQRV3iAAceIAAAAHocIkxU323.txt";
        byte[] fileBuff = "5ffff".getBytes(fdfsClientConfig.getCharset());
        int result = uploadClient.appendFile(groupName, appenderFileName, fileBuff);
        Assert.assertEquals(0, result);
        // TODO failed, may be changed
    }


    @Test
    public void test_modifyFile() throws FdfsException, Exception {
        String groupName = "group1";
        String appenderFileName = "M00/00/00/05vh0lQRV3iAAceIAAAAHocIkxU323.txt";
        byte[] fileBuff = "5ffff".getBytes(fdfsClientConfig.getCharset());
        int result = uploadClient.modifyFile(groupName, appenderFileName, 0, fileBuff);
        Assert.assertEquals(0, result);
     // TODO failed, may be changed
    }
}
