package com.zoo.fdfs.api;

import java.util.Map;


/**
 * 
 * @author yankai913@gmail.com
 * @date 2014-4-3
 */
public interface StorageClient {

    public String[] uploadFile(String localFileName, String fileExtName, Map<String, String> meta)
            throws FdfsException;


    public String[] uploadFile(byte[] fileBuff, int offset, int length, String fileExtName,
            Map<String, String> meta) throws FdfsException;


    public String[] uploadFile(String groupName, byte[] fileBuff, int offset, int length, String fileExtName,
            Map<String, String> meta) throws FdfsException;


    public String[] uploadFile(byte[] fileBuff, String fileExtName, Map<String, String> meta)
            throws FdfsException;


    public String[] uploadFile(String groupName, byte[] fileBuff, String fileExtName, Map<String, String> meta)
            throws FdfsException;


    public String[] uploadFile(String groupName, long fileSize, UploadCallback callback, String fileExtName,
            Map<String, String> meta) throws FdfsException;


    public String[] uploadFile(String groupName, String masterFileName, String prefixName,
            String localFileName, String fileExtName, Map<String, String> meta) throws FdfsException;


    public String[] uploadFile(String groupName, String masterFileName, String prefixName, byte[] fileBuff,
            String fileExtName, Map<String, String> meta) throws FdfsException;


    public String[] uploadFile(String groupName, String masterFileName, String prefixName, byte[] fileBuff,
            int offset, int length, String fileExtName, Map<String, String> meta) throws FdfsException;


    public String[] uploadFile(String groupName, String masterFileName, String prefixName, long fileSize,
            UploadCallback callback, String fileExtName, Map<String, String> meta) throws FdfsException;


    public String[] uploadAppenderFile(String localFileName, String fileExtName, Map<String, String> meta)
            throws FdfsException;


    public String[] uploadAppenderFile(byte[] fileBuff, int offset, int length, String fileExtName,
            Map<String, String> meta) throws FdfsException;


    public String[] uploadAppenderFile(String groupName, byte[] fileBuff, int offset, int length,
            String fileExtName, Map<String, String> meta) throws FdfsException;


    public String[] uploadAppenderFile(byte[] fileBuff, String fileExtName, Map<String, String> meta)
            throws FdfsException;


    public String[] uploadAppenderFile(String groupName, byte[] fileBuff, String fileExtName,
            Map<String, String> meta) throws FdfsException;


    public String[] uploadAppenderFile(String groupName, long fileSize, UploadCallback callback,
            String fileExtName, Map<String, String> meta) throws FdfsException;


    public int appendFile(String groupName, String appenderFileName, String localFileName)
            throws FdfsException;


    public int appendFile(String groupName, String appenderFileName, byte[] fileBuff) throws FdfsException;


    public int appendFile(String groupName, String appenderFileName, byte[] fileBuff, int offset, int length)
            throws FdfsException;


    public int appendFile(String groupName, String appenderFileName, long fileSize, UploadCallback callback)
            throws FdfsException;


    public int modifyFile(String groupName, String appenderFileName, long fileOffset, String localFileName)
            throws FdfsException;


    public int modifyFile(String groupName, String appenderFileName, long fileOffset, byte[] fileBuff)
            throws FdfsException;


    public int modifyFile(String groupName, String appenderFileName, long fileOffset, byte[] fileBuff,
            int bufferOffset, int bufferLength) throws FdfsException;


    public int modifyFile(String groupName, String appenderFileName, long fileOffset, long modifySize,
            UploadCallback callback) throws FdfsException;


    public int truncateFile(String groupName, String appenderFileName) throws FdfsException;


    public int truncateFile(String groupName, String appenderFileName, long truncatedFileSize)
            throws FdfsException;


    public FileInfo getFileInfo(String groupName, String remoteFileName) throws FdfsException;


    public FileInfo queryFileInfo(String groupName, String remoteFileName) throws FdfsException;


    public Map<String, String> getMetadata(String groupName, String remoteFileName) throws FdfsException;
    


    public byte[] downloadFile(String groupName, String remoteFileName) throws FdfsException;


    public byte[] downloadFile(String groupName, String remoteFileName, long fileOffset, long downloadBytes)
            throws FdfsException;


    public int downloadFile(String groupName, String remoteFileName, String localFileName)
            throws FdfsException;


    public int downloadFile(String groupName, String remoteFileName, String localFileName, long fileOffset,
            long downloadBytes) throws FdfsException;


    public int downloadFile(String groupName, String remoteFileName, DownloadCallback callback)
            throws FdfsException;


    public int downloadFile(String groupName, String remoteFileName, DownloadCallback callback,
            long fileOffset, long downloadBytes) throws FdfsException;

}
