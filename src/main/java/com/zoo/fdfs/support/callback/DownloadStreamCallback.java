package com.zoo.fdfs.support.callback;

import java.io.IOException;
import java.io.OutputStream;

import com.zoo.fdfs.api.DownloadCallback;


/**
 * 
 * @author yankai913@gmail.com
 * @date 2014-8-22
 */
public class DownloadStreamCallback implements DownloadCallback {

    private OutputStream out;
    private long currentBytes;


    public DownloadStreamCallback(OutputStream out) {
        this.out = out;
    }


    @Override
    public int recv(long fileSize, byte[] data, int bytes) {
        try {
            out.write(data, 0, bytes);
        } catch (IOException ex) {
            ex.printStackTrace();
            return -1;
        }

        currentBytes = currentBytes + bytes;
        if (this.currentBytes == fileSize) {
            this.currentBytes = 0;
        }

        return 0;
    }

}
