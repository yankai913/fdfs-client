package com.zoo.fdfs.api;

import java.io.IOException;
import java.io.OutputStream;


/**
 * 上传回调接口。
 * 
 * @author yankai913@gmail.com
 * @date 2014-8-21
 */
public interface UploadCallback {

    public int send(OutputStream out) throws IOException;

}
