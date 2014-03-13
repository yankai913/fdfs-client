package com.zoo.fdfs.support.tracker;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zoo.fdfs.common.ConcurrentHashSet;
import com.zoo.fdfs.config.FdfsClientConfig;


/**
 * 
 * @author yankai913@gmail.com
 * @date 2014-3-13
 */
public class TrackerManager {

    private static Logger logger = LoggerFactory.getLogger(TrackerManager.class);

    // Map<trackerAddr, Set<storageAddr>>
    private final Map<String, Set<String>> tracker2storageMap = new ConcurrentHashMap<String, Set<String>>();

    private final Set<String> storageServerAddrSet = new ConcurrentHashSet<String>();

    private FdfsClientConfig fdfsClientConfig;


    public TrackerManager(FdfsClientConfig fdfsClientConfig) {
        this.fdfsClientConfig = fdfsClientConfig;
    }


    public void start() {
        String trackerServerAddr = fdfsClientConfig.getTrackerServerAddr().trim();
        String[] trackerServerAddrList = trackerServerAddr.split(",");
        if (trackerServerAddrList.length < 1) {
            throw new IllegalArgumentException("trackerServerAddr invalid");
        }
        for (String addr : trackerServerAddrList) {

        }
    }


    public void refreshTracker() {

    }


    public void refreshStorage() {

    }


    public void shutdown() {

    }
}
