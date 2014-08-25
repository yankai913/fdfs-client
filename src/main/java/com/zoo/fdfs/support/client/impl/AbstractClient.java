package com.zoo.fdfs.support.client.impl;

import java.net.InetSocketAddress;

import com.zoo.fdfs.api.Connection;
import com.zoo.fdfs.api.FdfsClientConfig;
import com.zoo.fdfs.api.TrackerClient;
import com.zoo.fdfs.support.SimpleConnection;
import com.zoo.fdfs.support.SimpleConnectionPool;


/**
 * 
 * @author yankai913@gmail.com
 * @date 2014-8-21
 */
abstract class AbstractClient {

    private FdfsClientConfig fdfsClientConfig;

    private TrackerClient trackerClient;

    private SimpleConnectionPool connectionPool;


    public AbstractClient(FdfsClientConfig fdfsClientConfig, TrackerClient trackerClient) {
        this.fdfsClientConfig = fdfsClientConfig;
        this.trackerClient = trackerClient;
    }


    protected FdfsClientConfig getFdfsClientConfig() {
        return fdfsClientConfig;
    }


    protected TrackerClient getTrackerClient() {
        return trackerClient;
    }


    protected SimpleConnectionPool getConnectionPool() {
        return connectionPool;
    }


    protected void setConnectionPool(SimpleConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }


    protected Connection newConnection(InetSocketAddress inetSocketAddress) throws Exception {
        int readTimeout = getFdfsClientConfig().getReadTimeout();
        int connectTimeout = getFdfsClientConfig().getConnectTimeout();
        Connection con = new SimpleConnection(readTimeout);
        con.connect(inetSocketAddress, connectTimeout);
        return con;
    }
}
