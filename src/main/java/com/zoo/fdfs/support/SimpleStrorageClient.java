package com.zoo.fdfs.support;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.zoo.fdfs.api.Connection;
import com.zoo.fdfs.api.FdfsClientConfig;
import com.zoo.fdfs.api.FileInfo;
import com.zoo.fdfs.api.StorageClient;
import com.zoo.fdfs.api.StorageConfig;
import com.zoo.fdfs.api.TrackerClient;
import com.zoo.fdfs.common.Collections;
import com.zoo.fdfs.common.Strings;


/**
 * Prototype, Threadsafe
 * 
 * @author yankai913@gmail.com
 * @date 2014-4-3
 */
public class SimpleStrorageClient implements StorageClient {

    private FdfsClientConfig fdfsClientConfig;

    private TrackerClient trackerClient;

    private SimpleConnectionPool fetchConnectionPool;

    private SimpleConnectionPool storageConnectionPool;

    private SimpleConnectionPool updateConnectionPool;


    public SimpleStrorageClient(TrackerClient trackerClient, FdfsClientConfig fdfsClientConfig) {
        this.fdfsClientConfig = fdfsClientConfig;
        this.trackerClient = trackerClient;
    }


    public TrackerClient getTrackerClient() {
        return this.trackerClient;
    }


    public FdfsClientConfig getFdfsClientConfig() {
        return this.fdfsClientConfig;
    }


    public Connection getFetchableConnection(String groupName, String localFileName, String fileExtName,
            String masterFileName, String prefixName) {
        if (!Strings.isBlank(groupName) && !Strings.isBlank(masterFileName) && !Strings.isBlank(prefixName)) {
            throw new IllegalArgumentException("");
        }
        Set<StorageConfig> storageConfigSet = this.trackerClient.getFetchStorageSet(groupName, masterFileName);
        if (!Collections.isNotEmpty(storageConfigSet) ) {
            throw new IllegalStateException("storageConfigSet is blank");
        }
        int fetchSize = getFdfsClientConfig().getFetchPoolSize();
        for (int i = 0; i < fetchSize; i++) {
            
        }
        return null;
    }


    public Connection getUpdateableConnection() {
        return null;
    }


    public Connection getWriteableConnection() {
        return null;
    }


    @Override
    public String[] uploadFile(String localFileName, String fileExtName, Map<String, String> meta) {

        return null;
    }


    @Override
    public String[] uploadFile(byte[] fileBuff, int offset, int length, String fileExtName,
            Map<String, String> meta) {
        return null;
    }


    @Override
    public String[] uploadFile(String groupName, byte[] fileBuff, int offset, int length, String fileExtName,
            Map<String, String> meta) {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public String[] uploadFile(byte[] fileBuff, String fileExtName, Map<String, String> meta) {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public String[] uploadFile(String groupName, byte[] fileBuff, String fileExtName, Map<String, String> meta) {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public String[] uploadFile(String groupName, long fileSize, String fileExtName, Map<String, String> meta) {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public String[] uploadFile(String groupName, String masterFileName, String prefixName,
            String localFileName, String fileExtName, Map<String, String> meta) {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public String[] uploadFile(String groupName, String masterFileName, String prefixName, byte[] fileBuff,
            String fileExtName, Map<String, String> meta) {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public String[] uploadFile(String groupName, String masterFileName, String prefixName, byte[] fileBuff,
            int offset, int length, String fileExtName, Map<String, String> meta) {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public String[] uploadFile(String groupName, String masterFileName, String prefixName, long fileSize,
            String fileExtName, Map<String, String> meta) {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public String[] uploadAppenderFile(String localFileName, String fileExtName, Map<String, String> meta) {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public String[] uploadAppenderFile(byte[] fileBuff, int offset, int length, String fileExtName,
            Map<String, String> meta) {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public String[] uploadAppenderFile(String groupName, byte[] fileBuff, int offset, int length,
            String fileExtName, Map<String, String> meta) {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public String[] uploadAppenderFile(byte[] fileBuff, String fileExtName, Map<String, String> meta) {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public String[] uploadAppenderFile(String groupName, byte[] fileBuff, String fileExtName,
            Map<String, String> meta) {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public String[] uploadAppenderFile(String groupName, long fileSize, String fileExtName,
            Map<String, String> meta) {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public int appendFile(String groupName, String appenderFileName, String localFileName) {
        // TODO Auto-generated method stub
        return 0;
    }


    @Override
    public int appendFile(String groupName, String appenderFileName, byte[] fileBuff) {
        // TODO Auto-generated method stub
        return 0;
    }


    @Override
    public int append_file(String group_name, String appender_filename, byte[] file_buff, int offset,
            int length) {
        // TODO Auto-generated method stub
        return 0;
    }


    @Override
    public int appendFile(String groupName, String appenderFileName, long fileSize) {
        // TODO Auto-generated method stub
        return 0;
    }


    @Override
    public int modifyFile(String groupName, String appenderFileName, long fileOffset, String localFileName) {
        // TODO Auto-generated method stub
        return 0;
    }


    @Override
    public int modifyFile(String groupName, String appenderFileName, long fileOffset, byte[] fileBuff) {
        // TODO Auto-generated method stub
        return 0;
    }


    @Override
    public int modifyFile(String groupName, String appenderFileName, long fileOffset, byte[] fileBuff,
            int bufferOffset, int bufferLength) {
        // TODO Auto-generated method stub
        return 0;
    }


    @Override
    public int modifyFile(String groupName, String appenderFileName, long fileOffset, long modifySize) {
        // TODO Auto-generated method stub
        return 0;
    }


    @Override
    public int deleteFile(String groupName, String remoteFileName) {
        // TODO Auto-generated method stub
        return 0;
    }


    @Override
    public int truncateFile(String groupName, String appenderFileName) {
        // TODO Auto-generated method stub
        return 0;
    }


    @Override
    public int truncateFile(String groupName, String appenderFileName, long truncatedFileSize) {
        // TODO Auto-generated method stub
        return 0;
    }


    @Override
    public byte[] downloadFile(String groupName, String remoteFileName) {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public byte[] downloadFile(String groupName, String remoteFileName, long fileOffset, long downloadBytes) {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public int downloadFile(String groupName, String remoteFileName, String localFileName) {
        // TODO Auto-generated method stub
        return 0;
    }


    @Override
    public int downloadFile(String groupName, String remoteFileName, long fileOffset, long downloadBytes,
            String localFileName) {
        // TODO Auto-generated method stub
        return 0;
    }


    @Override
    public int downloadFileResult(String groupName, String remoteFileName) {
        // TODO Auto-generated method stub
        return 0;
    }


    @Override
    public int downloadFileResult(String groupName, String remoteFilename, long fileOffset, long downloadBytes) {
        // TODO Auto-generated method stub
        return 0;
    }


    @Override
    public Map<String, String> getMetadata(String groupName, String remoteFileName) {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public int setMetadataResult(String groupName, String remoteFileName, Map<String, String> meta,
            byte opFlag) {
        // TODO Auto-generated method stub
        return 0;
    }


    @Override
    public FileInfo getFileInfo(String groupName, String remoteFileName) {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public FileInfo queryFileInfo(String groupName, String remoteFileName) {
        // TODO Auto-generated method stub
        return null;
    }

}
