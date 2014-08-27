package com.zoo.fdfs.api;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 
 * @author yankai913@gmail.com
 * @date 2014-4-4
 */
public class FileInfo {
    protected String sourceIpAddr;
    protected long fileSize;
    protected Date createTimestamp;
    protected int crc32;


    public FileInfo(long fileSize, long createTimestamp, int crc32, String sourceIpAddr) {
        this.fileSize = fileSize;
        this.createTimestamp = new Timestamp(createTimestamp * 1000L);
        this.crc32 = crc32;
        this.sourceIpAddr = sourceIpAddr;
    }


    public String getSourceIpAddr() {
        return sourceIpAddr;
    }


    public void setSourceIpAddr(String sourceIpAddr) {
        this.sourceIpAddr = sourceIpAddr;
    }


    public long getFileSize() {
        return fileSize;
    }


    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }


    public Date getCreateTimestamp() {
        return createTimestamp;
    }


    public void setCreateTimestamp(Date createTimestamp) {
        this.createTimestamp = createTimestamp;
    }


    public int getCrc32() {
        return crc32;
    }


    public void setCrc32(int crc32) {
        this.crc32 = crc32;
    }


    public String toString() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return "source_ip_addr = " + this.sourceIpAddr + ", " + "file_size = " + this.fileSize + ", "
                + "create_timestamp = " + df.format(this.createTimestamp) + ", " + "crc32 = " + this.crc32;
    }
}