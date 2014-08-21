package com.zoo.fdfs.support;

import java.io.IOException;
import java.io.OutputStream;

import com.zoo.fdfs.api.UploadCallback;


/**
 * 上传一定偏移量的文件数据，可以支持做断点续传，供上传或者续传时回调。
 * 
 * @author yankai913@gmail.com
 * @date 2014-8-21
 */
public class UploadBufferCallback implements UploadCallback {

    private byte[] data;
    private int offset;
    private int length;


    public UploadBufferCallback(byte[] data, int offset, int length) {
        super();
        this.data = data;
        this.offset = offset;
        this.length = length;
    }


    @Override
    public int send(OutputStream out) throws IOException {
        out.write(this.data, this.offset, this.length);
        return 0;
    }

}
