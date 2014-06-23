package com.zoo.fdfs.api;

/**
 * 
 * @author yankai913@gmail.com
 * @date 2014-3-17
 */
public class FdfsException extends RuntimeException {

    private static final long serialVersionUID = -3231136192714616972L;


    public FdfsException() {
        super();
    }


    public FdfsException(String msg) {
        super(msg);
    }


    public FdfsException(Throwable t) {
        super(t);
    }


    public FdfsException(String msg, Throwable t) {
        super(msg, t);
    }
}
