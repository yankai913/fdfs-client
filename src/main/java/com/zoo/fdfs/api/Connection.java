package com.zoo.fdfs.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;


/**
 * 
 * @author yankai913@gmail.com
 * @date 2014-4-4
 */
public interface Connection {

    InetSocketAddress getRemoteAddress();


    void connect(InetSocketAddress inetSocketAddress, int connectTimeout) throws IOException;// timeout是毫秒


    boolean isConnected();


    byte[] read() throws IOException;


    void writeBytes(byte[] data) throws IOException;


    void close();


    long getReadTimestamp();


    long getWriteTimestamp();


    InputStream getInputStream() throws IOException;


    OutputStream getOutputStream() throws IOException;


    byte getStorePathIndex();


    void setStorePathIndex(byte storePathIndex);
}
