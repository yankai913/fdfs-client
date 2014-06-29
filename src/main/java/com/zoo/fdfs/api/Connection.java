package com.zoo.fdfs.api;

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


    void connect(InetSocketAddress inetSocketAddress, int connectTimeout) throws Exception;// timeout是毫秒


    boolean isConnected();


    byte[] read() throws Exception;


    void writeBytes(byte[] data) throws Exception;


    void close() throws Exception;


    long getReadTimestamp();


    long getWriteTimestamp();


    InputStream getInputStream() throws Exception;


    OutputStream getOutputStream() throws Exception;
}
