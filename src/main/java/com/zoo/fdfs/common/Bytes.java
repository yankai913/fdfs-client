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
    
}
