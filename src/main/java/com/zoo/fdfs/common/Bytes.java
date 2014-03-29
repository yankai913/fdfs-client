package com.zoo.fdfs.common;

/**
 * 
 * @author yankai913@gmail.com
 * @date 2014-3-13
 */
public class Bytes {

    public static void long2bytes(long v, byte[] dest) {
        long2bytes(v, dest, 0);
    }


    public static void long2bytes(long v, byte[] dest, int off) {
        dest[off + 7] = (byte) v;
        dest[off + 6] = (byte) (v >>> 8);
        dest[off + 5] = (byte) (v >>> 16);
        dest[off + 4] = (byte) (v >>> 24);
        dest[off + 3] = (byte) (v >>> 32);
        dest[off + 2] = (byte) (v >>> 40);
        dest[off + 1] = (byte) (v >>> 48);
        dest[off + 0] = (byte) (v >>> 56);
    }


    public static long bytes2long(byte[] b, int off) {
        long t = 0;
        t = t + ((b[off + 7] & 0xFF) << 0);
        t = t + ((b[off + 6] & 0xFF) << 8);
        t = t + ((b[off + 5] & 0xFF) << 16);
        t = t + ((b[off + 4] & 0xFF) << 24);
        t = t + ((b[off + 3] & 0xFF) << 32);
        t = t + ((b[off + 2] & 0xFF) << 40);
        t = t + ((b[off + 1] & 0xFF) << 48);
        t = t + ((b[off + 0] & 0xFF) << 56);
        return t;
    }


    public static int bytes2int(byte[] b, int off) {
        int t = 0;
        t = t + ((b[off + 3] & 0xFF) << 0);
        t = t + ((b[off + 2] & 0xFF) << 8);
        t = t + ((b[off + 1] & 0xFF) << 16);
        t = t + ((b[off + 0] & 0xFF) << 24);
        return t;
    }


    public static void truncToFill(byte[] src, int length, byte[] dest, int offset) {
        for (int i = 0; i < length; i++) {
            dest[i + offset] = src[i];
        }
    }


    public static void append(byte[] src, byte[] dest, int offset) {
        for (int i = 0; i < src.length; i++) {
            dest[i + offset] = src[i];
        }
    }


    public static void append(byte b, byte[] dest, int offset) {
        dest[offset] = b;
    }
}
