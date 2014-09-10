package com.zoo.fdfs.support;

import java.util.Map;

import com.zoo.fdfs.api.DownloadCallback;
import com.zoo.fdfs.api.FdfsClientConfig;
import com.zoo.fdfs.api.FdfsException;
import com.zoo.fdfs.api.FileInfo;
import com.zoo.fdfs.api.StorageClient;
import com.zoo.fdfs.api.TrackerClient;
import com.zoo.fdfs.api.UploadCallback;
import com.zoo.fdfs.support.client.DownloadClient;
import com.zoo.fdfs.support.client.UploadClient;
import com.zoo.fdfs.support.client.impl.DefaultDownloadClient;
import com.zoo.fdfs.support.client.impl.DefaultUploadClient;


/**
 * Prototype, Threadsafe
 * 
 * @author yankai913@gmail.com
 * @date 2014-4-3
 */
public class SimpleStrorageClient implements StorageClient {

    private DownloadClient downloadClient;

    private UploadClient uploadClient;


    public SimpleStrorageClient(TrackerClient trackerClient, FdfsClientConfig fdfsClientConfig) {
        this.downloadClient = new DefaultDownloadClient(fdfsClientConfig, trackerClient);
        this.uploadClient = new DefaultUploadClient(fdfsClientConfig, trackerClient);
    }


    @Override
    public String[] uploadFile(String localFileName, String fileExtName, Map<String, String> meta)
            throws FdfsException {
        return uploadClient.uploadFile(localFileName, fileExtName, meta);
    }


    @Override
    public String[] uploadFile(byte[] fileBuff, int offset, int length, String fileExtName,
            Map<String, String> meta) throws FdfsException {
        return uploadClient.uploadFile(fileBuff, offset, length, fileExtName, meta);
    }


    @Override
    public String[] uploadFile(String groupName, byte[] fileBuff, int offset, int length, String fileExtName,
            Map<String, String> meta) throws FdfsException {
        return uploadClient.uploadFile(groupName, fileBuff, offset, length, fileExtName, meta);
    }


    @Override
    public String[] uploadFile(byte[] fileBuff, String fileExtName, Map<String, String> meta)
            throws FdfsException {
        return uploadClient.uploadFile(fileBuff, fileExtName, meta);
    }


    @Override
    public String[] uploadFile(String groupName, byte[] fileBuff, String fileExtName, Map<String, String> meta)
            throws FdfsException {
        return uploadClient.uploadFile(groupName, fileBuff, fileExtName, meta);
    }


    @Override
    public String[] uploadFile(String groupName, long fileSize, UploadCallback callback, String fileExtName,
            Map<String, String> meta) throws FdfsException {
        return uploadClient.uploadFile(groupName, fileSize, callback, fileExtName, meta);
    }


    @Override
    public String[] uploadFile(String groupName, String masterFileName, String prefixName,
            String localFileName, String fileExtName, Map<String, String> meta) throws FdfsException {
        return uploadClient.uploadFile(groupName, masterFileName, prefixName, localFileName, fileExtName,
            meta);
    }


    @Override
    public String[] uploadFile(String groupName, String masterFileName, String prefixName, byte[] fileBuff,
            String fileExtName, Map<String, String> meta) throws FdfsException {
        return uploadClient.uploadFile(groupName, masterFileName, prefixName, fileBuff, fileExtName, meta);
    }


    @Override
    public String[] uploadFile(String groupName, String masterFileName, String prefixName, byte[] fileBuff,
            int offset, int length, String fileExtName, Map<String, String> meta) throws FdfsException {
        return uploadClient.uploadFile(groupName, masterFileName, prefixName, fileBuff, offset, length,
            fileExtName, meta);
    }


    @Override
    public String[] uploadFile(String groupName, String masterFileName, String prefixName, long fileSize,
            UploadCallback callback, String fileExtName, Map<String, String> meta) throws FdfsException {
        return uploadClient.uploadFile(groupName, masterFileName, prefixName, fileSize, callback,
            fileExtName, meta);
    }


    @Override
    public String[] uploadAppenderFile(String localFileName, String fileExtName, Map<String, String> meta)
            throws FdfsException {
        return uploadClient.uploadAppenderFile(localFileName, fileExtName, meta);
    }


    @Override
    public String[] uploadAppenderFile(byte[] fileBuff, int offset, int length, String fileExtName,
            Map<String, String> meta) throws FdfsException {
        return uploadClient.uploadAppenderFile(fileBuff, offset, length, fileExtName, meta);
    }


    @Override
    public String[] uploadAppenderFile(String groupName, byte[] fileBuff, int offset, int length,
            String fileExtName, Map<String, String> meta) throws FdfsException {
        return uploadClient.uploadAppenderFile(groupName, fileBuff, offset, length, fileExtName, meta);
    }


    @Override
    public String[] uploadAppenderFile(byte[] fileBuff, String fileExtName, Map<String, String> meta)
            throws FdfsException {
        return uploadClient.uploadAppenderFile(fileBuff, fileExtName, meta);
    }


    @Override
    public String[] uploadAppenderFile(String groupName, byte[] fileBuff, String fileExtName,
            Map<String, String> meta) throws FdfsException {
        return uploadClient.uploadAppenderFile(groupName, fileBuff, fileExtName, meta);
    }


    @Override
    public String[] uploadAppenderFile(String groupName, long fileSize, UploadCallback callback,
            String fileExtName, Map<String, String> meta) throws FdfsException {
        return uploadClient.uploadAppenderFile(groupName, fileSize, callback, fileExtName, meta);
    }


    @Override
    public int appendFile(String groupName, String appenderFileName, String localFileName)
            throws FdfsException {
        return uploadClient.appendFile(groupName, appenderFileName, localFileName);
    }


    @Override
    public int appendFile(String groupName, String appenderFileName, byte[] fileBuff) throws FdfsException {
        return uploadClient.appendFile(groupName, appenderFileName, fileBuff);
    }


    @Override
    public int appendFile(String groupName, String appenderFileName, byte[] fileBuff, int offset, int length)
            throws FdfsException {
        return uploadClient.appendFile(groupName, appenderFileName, fileBuff, offset, length);
    }


    @Override
    public int appendFile(String groupName, String appenderFileName, long fileSize, UploadCallback callback)
            throws FdfsException {
        return uploadClient.appendFile(groupName, appenderFileName, fileSize, callback);
    }


    @Override
    public int modifyFile(String groupName, String appenderFileName, long fileOffset, String localFileName)
            throws FdfsException {
        return uploadClient.modifyFile(groupName, appenderFileName, fileOffset, localFileName);
    }


    @Override
    public int modifyFile(String groupName, String appenderFileName, long fileOffset, byte[] fileBuff)
            throws FdfsException {
        return uploadClient.modifyFile(groupName, appenderFileName, fileOffset, fileBuff);
    }


    @Override
    public int modifyFile(String groupName, String appenderFileName, long fileOffset, byte[] fileBuff,
            int bufferOffset, int bufferLength) throws FdfsException {
        return uploadClient.modifyFile(groupName, appenderFileName, fileOffset, fileBuff, bufferOffset,
            bufferLength);
    }


    @Override
    public int modifyFile(String groupName, String appenderFileName, long fileOffset, long modifySize,
            UploadCallback callback) throws FdfsException {
        return uploadClient.modifyFile(groupName, appenderFileName, fileOffset, modifySize, callback);
    }


    @Override
    public int truncateFile(String groupName, String appenderFileName) throws FdfsException {
        return uploadClient.truncateFile(groupName, appenderFileName);
    }


    @Override
    public int truncateFile(String groupName, String appenderFileName, long truncatedFileSize)
            throws FdfsException {
        return uploadClient.truncateFile(groupName, appenderFileName, truncatedFileSize);
    }


    @Override
    public FileInfo getFileInfo(String groupName, String remoteFileName) throws FdfsException {
        return uploadClient.getFileInfo(groupName, remoteFileName);
    }


    @Override
    public FileInfo queryFileInfo(String groupName, String remoteFileName) throws FdfsException {
        return uploadClient.queryFileInfo(groupName, remoteFileName);
    }


    @Override
    public Map<String, String> getMetadata(String groupName, String remoteFileName) throws FdfsException {
        return uploadClient.getMetadata(groupName, remoteFileName);
    }


    @Override
    public byte[] downloadFile(String groupName, String remoteFileName) throws FdfsException {
        return downloadClient.downloadFile(groupName, remoteFileName);
    }


    @Override
    public byte[] downloadFile(String groupName, String remoteFileName, long fileOffset, long downloadBytes)
            throws FdfsException {
        return downloadClient.downloadFile(groupName, remoteFileName, fileOffset, downloadBytes);
    }


    @Override
    public int downloadFile(String groupName, String remoteFileName, String localFileName)
            throws FdfsException {
        return downloadClient.downloadFile(groupName, remoteFileName, localFileName);
    }


    @Override
    public int downloadFile(String groupName, String remoteFileName, String localFileName, long fileOffset,
            long downloadBytes) throws FdfsException {
        return downloadClient.downloadFile(groupName, remoteFileName, localFileName, fileOffset,
            downloadBytes);
    }


    @Override
    public int downloadFile(String groupName, String remoteFileName, DownloadCallback callback)
            throws FdfsException {
        return downloadClient.downloadFile(groupName, remoteFileName, callback);
    }


    @Override
    public int downloadFile(String groupName, String remoteFileName, DownloadCallback callback,
            long fileOffset, long downloadBytes) throws FdfsException {
        return downloadClient.downloadFile(groupName, remoteFileName, callback, fileOffset, downloadBytes);
    }

}
