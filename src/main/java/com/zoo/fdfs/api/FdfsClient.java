package com.zoo.fdfs.api;

import com.zoo.fdfs.api.StorageClient;
import com.zoo.fdfs.api.TrackerClient;


/**
 * 
 * @author yankai913@gmail.com
 * @date 2014-3-13
 */
public abstract class FdfsClient implements TrackerClient, StorageClient {

    public abstract void start();


    public abstract void shutdown();
}
