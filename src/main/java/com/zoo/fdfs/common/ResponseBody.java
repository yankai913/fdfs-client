package com.zoo.fdfs.common;

/**
 * 
 * @author yankai913@gmail.com
 * @date 2014-3-30
 */
public final class ResponseBody {
    
    private byte errorNo;

    private byte[] body;

    public ResponseBody(byte errorNo, byte[] body) {
        this.errorNo = errorNo;
        this.body = body;
    }
    
    public byte getErrorNo() {
        return errorNo;
    }


    public void setErrorNo(byte errorNo) {
        this.errorNo = errorNo;
    }


    public byte[] getBody() {
        return body;
    }


    public void setBody(byte[] body) {
        this.body = body;
    }

}
