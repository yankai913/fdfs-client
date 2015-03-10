package com.zoo.fdfs.support;

import java.util.Map;
import java.util.Set;

import com.zoo.fdfs.api.DownloadCallback;
import com.zoo.fdfs.api.FdfsClient;
import com.zoo.fdfs.api.FdfsException;
import com.zoo.fdfs.api.FileInfo;
import com.zoo.fdfs.api.GroupStat;
import com.zoo.fdfs.api.StorageClient;
import com.zoo.fdfs.api.StorageConfig;
import com.zoo.fdfs.api.StorageStat;
import com.zoo.fdfs.api.TrackerClient;
import com.zoo.fdfs.api.TrackerGroup;
import com.zoo.fdfs.api.UploadCallback;


/**
 * 
 * @author yankai913@gmail.com
 * @date 2014-4-4
 */
public class SimpleFdfsClient extends FdfsClient {

    private TrackerClient trackerClient;

    private StorageClient strorageClient;


    public SimpleFdfsClient(TrackerClient trackerClient, StorageClient strorageClient) {
        this.trackerClient = trackerClient;
        this.strorageClient = strorageClient;
    }


    public TrackerClient getTrackerClient() {
        return trackerClient;
    }


    public StorageClient getStorageClient() {
        return strorageClient;
    }


    @Override
    public String[] uploadFile(String localFileName, String fileExtName, Map<String, String> meta)
            throws FdfsException {
        return null;
    }


    @Override
    public String[] uploadFile(byte[] fileBuff, int offset, int length, String fileExtName,
            Map<String, String> meta) throws FdfsException {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public String[] uploadFile(String groupName, byte[] fileBuff, int offset, int length, String fileExtName,
            Map<String, String> meta) throws FdfsException {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public String[] uploadFile(byte[] fileBuff, String fileExtName, Map<String, String> meta)
            throws FdfsException {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public String[] uploadFile(String groupName, byte[] fileBuff, String fileExtName, Map<String, String> meta)
            throws FdfsException {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public String[] uploadFile(String groupName, long fileSize, UploadCallback callback, String fileExtName,
            Map<String, String> meta) throws FdfsException {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public String[] uploadFile(String groupName, String masterFileName, String prefixName,
            String localFileName, String fileExtName, Map<String, String> meta) throws FdfsException {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public String[] uploadFile(String groupName, String masterFileName, String prefixName, byte[] fileBuff,
            String fileExtName, Map<String, String> meta) throws FdfsException {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public String[] uploadFile(String groupName, String masterFileName, String prefixName, byte[] fileBuff,
            int offset, int length, String fileExtName, Map<String, String> meta) throws FdfsException {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public String[] uploadFile(String groupName, String masterFileName, String prefixName, long fileSize,
            UploadCallback callback, String fileExtName, Map<String, String> meta) throws FdfsException {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public String[] uploadAppenderFile(String localFileName, String fileExtName, Map<String, String> meta)
            throws FdfsException {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public String[] uploadAppenderFile(byte[] fileBuff, int offset, int length, String fileExtName,
            Map<String, String> meta) throws FdfsException {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public String[] uploadAppenderFile(String groupName, byte[] fileBuff, int offset, int length,
            String fileExtName, Map<String, String> meta) throws FdfsException {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public String[] uploadAppenderFile(byte[] fileBuff, String fileExtName, Map<String, String> meta)
            throws FdfsException {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public String[] uploadAppenderFile(String groupName, byte[] fileBuff, String fileExtName,
            Map<String, String> meta) throws FdfsException {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public String[] uploadAppenderFile(String groupName, long fileSize, UploadCallback callback,
            String fileExtName, Map<String, String> meta) throws FdfsException {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public int appendFile(String groupName, String appenderFileName, String localFileName)
            throws FdfsException {
        // TODO Auto-generated method stub
        return 0;
    }


    @Override
    public int appendFile(String groupName, String appenderFileName, byte[] fileBuff) throws FdfsException {
        // TODO Auto-generated method stub
        return 0;
    }


    @Override
    public int appendFile(String groupName, String appenderFileName, byte[] fileBuff, int offset, int length)
            throws FdfsException {
        // TODO Auto-generated method stub
        return 0;
    }


    @Override
    public int appendFile(String groupName, String appenderFileName, long fileSize, UploadCallback callback)
            throws FdfsException {
        // TODO Auto-generated method stub
        return 0;
    }


    @Override
    public int modifyFile(String groupName, String appenderFileName, long fileOffset, String localFileName)
            throws FdfsException {
        // TODO Auto-generated method stub
        return 0;
    }


    @Override
    public int modifyFile(String groupName, String appenderFileName, long fileOffset, byte[] fileBuff)
            throws FdfsException {
        // TODO Auto-generated method stub
        return 0;
    }


    @Override
    public int modifyFile(String groupName, String appenderFileName, long fileOffset, byte[] fileBuff,
            int bufferOffset, int bufferLength) throws FdfsException {
        // TODO Auto-generated method stub
        return 0;
    }


    @Override
    public int modifyFile(String groupName, String appenderFileName, long fileOffset, long modifySize,
            UploadCallback callback) throws FdfsException {
        // TODO Auto-generated method stub
        return 0;
    }


    @Override
    public int truncateFile(String groupName, String appenderFileName) throws FdfsException {
        // TODO Auto-generated method stub
        return 0;
    }


    @Override
    public int truncateFile(String groupName, String appenderFileName, long truncatedFileSize)
            throws FdfsException {
        // TODO Auto-generated method stub
        return 0;
    }


    @Override
    public FileInfo getFileInfo(String groupName, String remoteFileName) throws FdfsException {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public FileInfo queryFileInfo(String groupName, String remoteFileName) throws FdfsException {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public Map<String, String> getMetadata(String groupName, String remoteFileName) throws FdfsException {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public byte[] downloadFile(String groupName, String remoteFileName) throws FdfsException {
        return strorageClient.downloadFile(groupName, remoteFileName);
    }


    @Override
    public byte[] downloadFile(String groupName, String remoteFileName, long fileOffset, long downloadBytes)
            throws FdfsException {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public int downloadFile(String groupName, String remoteFileName, String localFileName)
            throws FdfsException {
        // TODO Auto-generated method stub
        return 0;
    }


    @Override
    public int downloadFile(String groupName, String remoteFileName, String localFileName, long fileOffset,
            long downloadBytes) throws FdfsException {
        // TODO Auto-generated method stub
        return 0;
    }


    @Override
    public int downloadFile(String groupName, String remoteFileName, DownloadCallback callback)
            throws FdfsException {
        // TODO Auto-generated method stub
        return 0;
    }


    @Override
    public int downloadFile(String groupName, String remoteFileName, DownloadCallback callback,
            long fileOffset, long downloadBytes) throws FdfsException {
        // TODO Auto-generated method stub
        return 0;
    }


    @Override
    public Set<String> getTrackerServerAddrSet() throws FdfsException {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public StorageConfig getStoreStorageOne(String groupName) throws FdfsException {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public Set<StorageConfig> getStoreStorageSet(String groupName) throws FdfsException {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public Set<StorageConfig> getFetchStorageSet(String groupName, String fileName) throws FdfsException {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public GroupStat[] listGroups() throws FdfsException {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public StorageStat[] listStorages(String groupName, String storageServerAddr) throws FdfsException {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public StorageConfig getUpdateStorage(String groupName, String fileName) throws FdfsException {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public boolean deleteStorage(TrackerGroup trackerGroup, String groupName, String storageServerAddr)
            throws FdfsException {
        // TODO Auto-generated method stub
        return false;
    }


    @Override
    public boolean deleteStorage(String groupName, String storageServerAddr) throws FdfsException {
        // TODO Auto-generated method stub
        return false;
    }


    @Override
    public void start() {

    }


    @Override
    public void shutdown() {

    }

}
