package com.zoo.fdfs.api;

import java.util.Map;


/**
 * 
 * @author yankai913@gmail.com
 * @date 2014-4-3
 */
public interface StorageClient {

    public TrackerClient getTrackerClient();


    public FdfsClientConfig getFdfsClientConfig();


    
    public String[] uploadFile(String localFileName, String fileExtName, Map<String, String> meta);
    public String[] uploadFile(String localFileName, String fileExtName, Map<String, String> meta, String groupName);
            

    public String[] uploadFile(byte[] fileBuff, int offset, int length, String fileExtName, Map<String, String> meta);
    public String[] uploadFile(byte[] fileBuff, int offset, int length, String fileExtName, Map<String, String> meta, String groupName);
            

    public String[] uploadFile(byte[] fileBuff, String fileExtName, Map<String, String> meta);
    public String[] uploadFile(byte[] fileBuff, String fileExtName, Map<String, String> meta, String groupName);


    public String[] uploadFile(String groupName, String masterFileName, String prefixName, String localFileName, String fileExtName, Map<String, String> meta);
    public String[] uploadFile(String groupName, String masterFileName, String prefixName, byte[] fileBuff, String fileExtName, Map<String, String> meta);
    public String[] uploadFile(String groupName, String masterFileName, String prefixName, byte[] fileBuff, int offset, int length, String fileExtName, Map<String, String> meta);
    
    //callback
    public String[] uploadFile(String groupName, String masterFileName, String prefixName, long fileSize, UploadCallback callback, String fileExtName, Map<String, String> meta);
    

    
    public String[] uploadAppenderFile(String localFileName, String fileExtName, Map<String, String> meta);
    public String[] uploadAppenderFile(String localFileName, String fileExtName, Map<String, String> meta, String groupName);

    
    public String[] uploadAppenderFile(byte[] fileBuff, int offset, int length, String fileExtName, Map<String, String> meta);
    public String[] uploadAppenderFile(byte[] fileBuff, int offset, int length, String fileExtName, Map<String, String> meta, String groupName);
            

    public String[] uploadAppenderFile(byte[] fileBuff, String fileExtName, Map<String, String> meta);
    public String[] uploadAppenderFile(byte[] fileBuff, String fileExtName, Map<String, String> meta, String groupName);
            
    public String[] uploadAppenderFile(String groupName, long fileSize, UploadCallback callback, String fileExtName, Map<String, String> meta);
    
    
    
    public int appendFile(String groupName, String appenderFileName, String localFileName);
    public int appendFile(String groupName, String appenderFileName, byte[] fileBuff);
    public int appendFile(String groupName, String appenderFilename, byte[] fileBuff, int offset, int length);
    public int appendFile(String groupName, String appenderFileName, long fileSize, UploadCallback callback);

    

    public int modifyFile(String groupName, String appenderFileName, long fileOffset, String localFileName);
    public int modifyFile(String groupName, String appenderFileName, long fileOffset, byte[] fileBuff);
    public int modifyFile(String groupName, String appenderFileName, long fileOffset, byte[] fileBuff, int bufferOffset, int bufferLength);
    public int modifyFile(String groupName, String appenderFileName, long fileOffset, long modifySize, UploadCallback callback);


    public int deleteFile(String groupName, String remoteFileName);
    
    
    public int truncateFile(String groupName, String appenderFileName);
    public int truncateFile(String groupName, String appenderFileName, long truncatedFileSize);


    public byte[] downloadFile(String groupName, String remoteFileName);
    public byte[] downloadFile(String groupName, String remoteFileName, long fileOffset, long downloadBytes);


    public int downloadFile(String groupName, String remoteFileName, String localFileName);
    public int downloadFile(String groupName, String remoteFileName, String localFileName, long fileOffset, long downloadBytes);
            


    public int downloadFile(String groupName, String remoteFileName, DownloadCallback callback);
    public int downloadFile(String groupName, String remoteFileName, DownloadCallback callback, long fileOffset, long downloadBytes);


    public Map<String, String> getMetadata(String groupName, String remoteFileName);
    public int setMetadata(String groupName, String remoteFileName, Map<String, String> meta, byte opFlag);
            

    public FileInfo getFileInfo(String groupName, String remoteFileName);
    public FileInfo queryFileInfo(String groupName, String remoteFileName);
}
