package com.zoo.fdfs.support;

import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zoo.fdfs.FdfsClient;
import com.zoo.fdfs.common.ConcurrentHashSet;
import com.zoo.fdfs.config.FdfsClientConfig;
import com.zoo.fdfs.support.storage.StorageManager;
import com.zoo.fdfs.support.tracker.TrackerManager;


/**
 * Prototype, ThreadSafe
 * 
 * @author yankai913@gmail.com
 * @date 2014-3-13
 */
public class DefaultFdfsClient implements FdfsClient {

    private static Logger logger = LoggerFactory.getLogger(DefaultFdfsClient.class);

    private FdfsClientConfig fdfsClientConfig;

    private TrackerManager trackerManager;

    private StorageManager storageManager;


    public DefaultFdfsClient() {

    }


    public void start() {
        // TODO Auto-generated method stub
    }


    @Override
    public void shutdown() {
        // TODO Auto-generated method stub

    }

}
