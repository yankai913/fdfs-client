package com.zoo.fdfs.support.client;

import com.zoo.fdfs.api.Constants;
import com.zoo.fdfs.api.DownloadCallback;
import com.zoo.fdfs.api.FdfsException;


/**
 * client for download
 * 
 * @author yankai913@gmail.com
 * @date 2014-8-21
 */
public interface DownloadClient {

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
