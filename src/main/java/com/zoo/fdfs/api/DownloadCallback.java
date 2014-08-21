package com.zoo.fdfs.api;

/**
 * 
 * @author yankai913@gmail.com
 * @date 2014-8-21
 */
public interface DownloadCallback {

    public int recv(long fileSize, byte[] data, int bytes);

}
