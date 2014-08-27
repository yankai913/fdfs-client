package com.zoo.fdfs.common;

/**
 * 自定义byte数组
 * 
 * @author yankai913@gmail.com
 * @date 2014-3-29
 */
public final class ReadByteArrayFragment {

    private byte[] data;

    private int readIndex = 0;


    public ReadByteArrayFragment(byte[] data) {
        this.data = data;
    }


    private void checkSize(int offset) {
        if (readIndex + offset > data.length) {
            throw new IllegalStateException("index out of size");
        }
    }


    public byte readByte() {
        int offset = 1;
        checkSize(offset);
        return data[readIndex++];
    }


    public byte[] readBytes(int length) {
        checkSize(length);
        byte[] buf = new byte[length];
        for (int i = 0; i < length; i++) {
            buf[i] = data[readIndex++];
        }
        return buf;
    }


    public long readLong() {
        checkSize(8);
        long t = 0;
        t = t + ((data[readIndex + 7] & 0xFF) << 0);
        t = t + ((data[readIndex + 6] & 0xFF) << 8);
        t = t + ((data[readIndex + 5] & 0xFF) << 16);
        t = t + ((data[readIndex + 4] & 0xFF) << 24);
        t = t + ((data[readIndex + 3] & 0xFF) << 32);
        t = t + ((data[readIndex + 2] & 0xFF) << 40);
        t = t + ((data[readIndex + 1] & 0xFF) << 48);
        t = t + ((data[readIndex + 0] & 0xFF) << 56);
        readIndex = readIndex + 8;
        return t;
    }


    public int readInt() {
        checkSize(4);
        int t = 0;
        t = t + ((data[readIndex + 3] & 0xFF) << 0);
        t = t + ((data[readIndex + 2] & 0xFF) << 8);
        t = t + ((data[readIndex + 1] & 0xFF) << 16);
        t = t + ((data[readIndex + 0] & 0xFF) << 24);
        readIndex = readIndex + 4;
        return t;
    }


    public String readString(int offset, int length) {
        checkSize(offset + length);
        String str = new String(data, readIndex + offset, length);
        readIndex = readIndex + offset + length;
        return str;
    }

    public String readString(int length) {
        checkSize(length);
        String str = new String(data, readIndex, length);
        readIndex = readIndex + length;
        return str;
    }
    

    public void skip(int offset) {
        readIndex = readIndex + offset;
    }
}
