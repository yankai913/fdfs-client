package com.zoo.fdfs.support;

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


    public SimpleConnection(int readTimeout) {
        this.socket = new Socket();
        try {
            this.socket.setSoTimeout(readTimeout);
            this.socket.setSendBufferSize(64 * 1024);
            this.socket.setReceiveBufferSize(64 * 1024);
        }
        catch (SocketException e) {
            e.printStackTrace();
        }
    }


    @Override
    public InetSocketAddress getRemoteAddress() {
        return this.inetSocketAddress;
    }


    @Override
    public void connect(InetSocketAddress inetSocketAddress, int readTimeout) throws Exception {
        this.inetSocketAddress = inetSocketAddress;
        this.socket.connect(getRemoteAddress(), readTimeout);
    }


    @Override
    public boolean isConnected() {
        return this.socket.isClosed();
    }


    @Override
    public byte[] read() throws Exception {
        this.lastReadTimestamp = System.currentTimeMillis();
        InputStream is = this.socket.getInputStream();
        int available = is.available();
        byte[] arr = new byte[available];
        is.read(arr);
        return arr;
    }


    @Override
    public void writeBytes(byte[] data) throws Exception {
        this.lastWriteTimestamp = System.currentTimeMillis();
        OutputStream os = this.socket.getOutputStream();
        os.write(data);
    }


    @Override
    public void close() throws Exception {
        if (this.socket != null && !this.socket.isClosed()) {
            this.socket.close();
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

}
