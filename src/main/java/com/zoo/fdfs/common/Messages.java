package com.zoo.fdfs.common;

/**
 * 
 * @author yankai913@gmail.com
 * @date 2014-3-13
 */
public class Messages {

    /**
     * <h2>header part, 10 bytes</h2>
     * |---8bytes(pkgLength)---|---1byte(cmd)---|---1byte(errno)---|
     * */
    public static final int FDFS_PROTO_PKG_LEN_SIZE = 8;
    public static final int FDFS_PROTO_CMD_SIZE = 1;
    public static final int FDFS_PROTO_ERR_NO_SIZE = 1;


    public static byte[] createHeader(long pkgLength, byte cmd, byte errno) {
        byte[] header = new byte[FDFS_PROTO_PKG_LEN_SIZE + FDFS_PROTO_CMD_SIZE + FDFS_PROTO_ERR_NO_SIZE];

        Bytes.long2bytes(pkgLength, header);
        header[FDFS_PROTO_PKG_LEN_SIZE + FDFS_PROTO_CMD_SIZE - 1] = cmd;
        header[FDFS_PROTO_PKG_LEN_SIZE + FDFS_PROTO_CMD_SIZE + FDFS_PROTO_ERR_NO_SIZE - 1] = errno;
        return header;
    }
}
