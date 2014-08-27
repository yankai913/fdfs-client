package com.zoo.fdfs.support.callback;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.zoo.fdfs.api.UploadCallback;


/**
 * 
 * @author yankai913@gmail.com
 * @date 2014-8-25
 */
public class UploadStreamCallback implements UploadCallback {

    private InputStream inputStream; // input stream for reading
    private long fileSize = 0; // size of the uploaded file


    public UploadStreamCallback(InputStream inputStream, long fileSize) {
        this.inputStream = inputStream;
        this.fileSize = fileSize;
    }


    public int send(OutputStream out) throws IOException {
        long remainBytes = fileSize;
        byte[] buff = new byte[256 * 1024];
        int bytes;
        while (remainBytes > 0) {
            try {
                if ((bytes = inputStream.read(buff, 0, remainBytes > buff.length ? buff.length : (int) remainBytes)) < 0) {
                    return -1;
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                return -1;
            }

            out.write(buff, 0, bytes);
            remainBytes -= bytes;
        }

        return 0;
    }

}
