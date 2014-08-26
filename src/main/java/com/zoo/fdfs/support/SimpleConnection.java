package com.zoo.fdfs.support;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

import com.zoo.fdfs.api.Connection;


/**
 * Prototype, Threadsafe
 * 
 * @author yankai913@gmail.com
 * @date 2014-4-4
 */
public class SimpleConnection implements Connection {

    private InetSocketAddress inetSocketAddress;

    private Socket socket;

    private long lastReadTimestamp;

    private long lastWriteTimestamp;

    private byte storePathIndex;


    public SimpleConnection(int readTimeout) throws SocketException {
        this.socket = new Socket();
        this.socket.setSoTimeout(readTimeout);// milliseconds
        this.socket.setSendBufferSize(64 * 1024);
        this.socket.setReceiveBufferSize(64 * 1024);
    }


    @Override
    public InetSocketAddress getRemoteAddress() {
        return this.inetSocketAddress;
    }


    @Override
    public void connect(InetSocketAddress inetSocketAddress, int connectTimeout) throws IOException {
        this.inetSocketAddress = inetSocketAddress;
        this.socket.connect(getRemoteAddress(), connectTimeout);// milliseconds
    }


    @Override
    public boolean isConnected() {
        return this.socket.isClosed();
    }


    @Override
    public byte[] read() throws IOException {
        this.lastReadTimestamp = System.currentTimeMillis();
        InputStream is = this.socket.getInputStream();
        int available = is.available();
        byte[] arr = new byte[available];
        is.read(arr);
        return arr;
    }


    @Override
    public void writeBytes(byte[] data) throws IOException {
        this.lastWriteTimestamp = System.currentTimeMillis();
        OutputStream os = this.socket.getOutputStream();
        os.write(data);
    }


    @Override
    public void close() {
        try {
            if (this.socket != null && !this.socket.isClosed()) {
                this.socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public long getReadTimestamp() {
        return this.lastReadTimestamp;
    }


    @Override
    public long getWriteTimestamp() {
        return this.lastWriteTimestamp;
    }


    @Override
    public InputStream getInputStream() throws IOException {
        return this.socket.getInputStream();
    }


    @Override
    public OutputStream getOutputStream() throws IOException {
        return this.socket.getOutputStream();
    }


    @Override
    public byte getStorePathIndex() {
        return this.storePathIndex;
    }


    @Override
    public void setStorePathIndex(byte storePathIndex) {
        this.storePathIndex = storePathIndex;
    }
}
