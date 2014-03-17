package com.zoo.fdfs.api;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.Date;

import com.zoo.fdfs.common.Bytes;


/**
 * 
 * @author yankai913@gmail.com
 * @date 2014-3-17
 */
public abstract class BaseStat {

    public abstract void setFields(byte[] bs, int offset);

    protected String charset = "ISO8859-1";

    public BaseStat(String charset) {
        this.charset = charset;
    }

    public static class FieldMeta {
        protected String name;
        protected int offset;
        protected int size;


        public FieldMeta(String name, int offset, int size) {
            this.name = name;
            this.offset = offset;
            this.size = size;
        }
    }


    protected String readString(byte[] bs, int offset, FieldMeta filedInfo) {
        try {
            return new String(bs, offset + filedInfo.offset, filedInfo.size, this.charset);
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }


    protected long readLong(byte[] bs, int offset, FieldMeta filedInfo) {
        return Bytes.bytes2long(bs, offset + filedInfo.offset);
    }


    protected int readInt(byte[] bs, int offset, FieldMeta filedInfo) {
        return Bytes.bytes2int(bs, offset + filedInfo.offset);
    }


    protected byte readByte(byte[] bs, int offset, FieldMeta filedInfo) {
        return bs[offset + filedInfo.offset];
    }


    protected boolean readBoolean(byte[] bs, int offset, FieldMeta filedInfo) {
        return bs[offset + filedInfo.offset] != 0;
    }


    protected Date readDate(byte[] bs, int offset, FieldMeta filedInfo) {
        long milis = Bytes.bytes2long(bs, offset + filedInfo.offset) * 1000;
        Timestamp timestamp = new Timestamp(milis);
        return timestamp;
    }
}
