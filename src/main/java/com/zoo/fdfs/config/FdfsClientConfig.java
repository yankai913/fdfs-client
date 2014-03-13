package com.zoo.fdfs.config;

/**
 * 
 * @author yankai913@gmail.com
 * @date 2014-3-13
 */
public class FdfsClientConfig {

    private long connectTimeout = 5 * 1000;// millisecond

    private long readTimeout = 30 * 1000;

    private String charset = "ISO8859-1";

    private int httpTrackerHttpPort = 8002;

    private boolean httpAntiStealToker = false;

    private String httpSecretKey = "FastDFS1234567890";

    private String trackerServerAddr;// 192.168.1.10:22122,192.168.1.11:22122

    /** between client and storaServer can create many connections, default 1; */
    private int sizePerStorage = 1;


    public FdfsClientConfig(String trackerServerAddr) {
        this.trackerServerAddr = trackerServerAddr;
    }


    public FdfsClientConfig(String trackerServerAddr, int sizePerStorage) {
        this.sizePerStorage = sizePerStorage;
        this.trackerServerAddr = trackerServerAddr;
    }


    public long getConnectTimeout() {
        return connectTimeout;
    }


    public void setConnectTimeout(long connectTimeout) {
        this.connectTimeout = connectTimeout;
    }


    public long getReadTimeout() {
        return readTimeout;
    }


    public void setReadTimeout(long readTimeout) {
        this.readTimeout = readTimeout;
    }


    public String getCharset() {
        return charset;
    }


    public void setCharset(String charset) {
        this.charset = charset;
    }


    public int getHttpTrackerHttpPort() {
        return httpTrackerHttpPort;
    }


    public void setHttpTrackerHttpPort(int httpTrackerHttpPort) {
        this.httpTrackerHttpPort = httpTrackerHttpPort;
    }


    public boolean isHttpAntiStealToker() {
        return httpAntiStealToker;
    }


    public void setHttpAntiStealToker(boolean httpAntiStealToker) {
        this.httpAntiStealToker = httpAntiStealToker;
    }


    public String getHttpSecretKey() {
        return httpSecretKey;
    }


    public void setHttpSecretKey(String httpSecretKey) {
        this.httpSecretKey = httpSecretKey;
    }


    public String getTrackerServerAddr() {
        return trackerServerAddr;
    }


    public void setTrackerServerAddr(String trackerServerAddr) {
        this.trackerServerAddr = trackerServerAddr;
    }


    public int getSizePerStorage() {
        return sizePerStorage;
    }


    public void setSizePerStorage(int sizePerStorage) {
        this.sizePerStorage = sizePerStorage;
    }

}
