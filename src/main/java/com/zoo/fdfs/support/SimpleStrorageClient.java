package com.zoo.fdfs.support;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.Set;

import static com.zoo.fdfs.api.Constants.FDFS_GROUP_NAME_MAX_LEN;
import static com.zoo.fdfs.api.Constants.STORAGE_PROTO_CMD_DOWNLOAD_FILE;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zoo.fdfs.api.Connection;
import com.zoo.fdfs.api.Constants;
import com.zoo.fdfs.api.FdfsClientConfig;
import com.zoo.fdfs.api.FdfsException;
import com.zoo.fdfs.api.FileInfo;
import com.zoo.fdfs.api.StorageClient;
import com.zoo.fdfs.api.StorageConfig;
import com.zoo.fdfs.api.TrackerClient;
import com.zoo.fdfs.common.Bytes;
import com.zoo.fdfs.common.Circle;
import com.zoo.fdfs.common.Collections;
import com.zoo.fdfs.common.IOs;
import com.zoo.fdfs.common.Messages;
import com.zoo.fdfs.common.ResponseBody;
import com.zoo.fdfs.common.Strings;
import com.zoo.fdfs.common.WriteByteArrayFragment;


/**
 * Prototype, Threadsafe
 * 
 * @author yankai913@gmail.com
 * @date 2014-4-3
 */
public class SimpleStrorageClient implements StorageClient {

    private static final Logger logger = LoggerFactory.getLogger(SimpleStrorageClient.class);

    private FdfsClientConfig fdfsClientConfig;

    private TrackerClient trackerClient;


    public SimpleStrorageClient(TrackerClient trackerClient, FdfsClientConfig fdfsClientConfig) {
        this.fdfsClientConfig = fdfsClientConfig;
        this.trackerClient = trackerClient;
    }


    public TrackerClient getTrackerClient() {
        return trackerClient;
    }


    public FdfsClientConfig getFdfsClientConfig() {
        return fdfsClientConfig;
    }

}
