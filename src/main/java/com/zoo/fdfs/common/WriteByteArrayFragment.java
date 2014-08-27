package com.zoo.fdfs.common;

/**
 * 自定义byte数组
 * 
 * @author yankai913@gmail.com
 * @date 2014-3-29
 */
public final class WriteByteArrayFragment {

    private byte[] data;

    private int writeIndex = 0;


    public WriteByteArrayFragment(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("invalid size");
        }
        this.data = new byte[size];
    }


    private void checkSize(int offset) {
        if (writeIndex + offset > data.length) {
            throw new IllegalStateException("index out of size");
        }
    }


    public void writeByte(byte b) {
        int offset = 1;
        checkSize(offset);
        data[writeIndex++] = b;
    }


    public void writeBytes(byte[] buf) {
        checkSize(buf.length);
        for (int i = 0; i < buf.length; i++) {
            data[writeIndex++] = buf[i];
        }
    }


    public void writeLimitedBytes(byte[] buf, int limitLength) {
        checkSize(limitLength);
        for (int i = 0; i < limitLength; i++) {
            if (i < buf.length) {
                data[writeIndex++] = buf[i];
            } else {
                data[writeIndex++] = 0;
            }
        }
    }


    public void writeLong(long v) {
        checkSize(8);
        for (int i = 0; i < 8; i++) {
            data[writeIndex++] = (byte) (v >>> (56 - i * 8));
        }
    }


    public void writeInt(int v) {
        checkSize(4);
        for (int i = 0; i < 4; i++) {
            data[writeIndex++] = (byte) (v >>> (24 - i * 8));
        }
    }


    public byte[] getData() {
        return data;
    }


    public int getWriteIndex() {
        return writeIndex;
    }
}
