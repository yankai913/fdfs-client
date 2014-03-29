package com.zoo.fdfs.common;

import static com.zoo.fdfs.api.Contants.FDFS_PROTO_CMD_SIZE;
import static com.zoo.fdfs.api.Contants.FDFS_PROTO_ERR_NO_SIZE;
import static com.zoo.fdfs.api.Contants.FDFS_PROTO_PKG_LEN_SIZE;
import static com.zoo.fdfs.api.Contants.FDFS_GROUP_NAME_MAX_LEN;

import com.zoo.fdfs.api.Contants;


/**
 * 
 * @author yankai913@gmail.com
 * @date 2014-3-13
 */
public class Messages {

    public static byte[] createHeader(String groupName, String fileName, String charsetName, byte cmd,
            byte errno) {
        byte[] groupNameByteArr = null;
        byte[] fileNameByteArr = null;
        try {
            groupNameByteArr = groupName.getBytes(charsetName);
            fileNameByteArr = fileName.getBytes(charsetName);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        long groupNameByteArrLength =
                groupNameByteArr.length <= FDFS_GROUP_NAME_MAX_LEN ? groupNameByteArr.length
                        : FDFS_GROUP_NAME_MAX_LEN;
        long pkgLength = groupNameByteArrLength + fileNameByteArr.length;
        byte[] header = new byte[FDFS_PROTO_PKG_LEN_SIZE + FDFS_PROTO_CMD_SIZE + FDFS_PROTO_ERR_NO_SIZE];
        Bytes.long2bytes(pkgLength, header);
        header[FDFS_PROTO_PKG_LEN_SIZE + FDFS_PROTO_CMD_SIZE - 1] = cmd;
        header[FDFS_PROTO_PKG_LEN_SIZE + FDFS_PROTO_CMD_SIZE + FDFS_PROTO_ERR_NO_SIZE - 1] = errno;
        return header;
    }


    public static byte[] createHeader(long pkgLength, byte cmd, byte errno) {
        byte[] header = new byte[FDFS_PROTO_PKG_LEN_SIZE + FDFS_PROTO_CMD_SIZE + FDFS_PROTO_ERR_NO_SIZE];

        Bytes.long2bytes(pkgLength, header);
        header[FDFS_PROTO_PKG_LEN_SIZE + FDFS_PROTO_CMD_SIZE - 1] = cmd;
        header[FDFS_PROTO_PKG_LEN_SIZE + FDFS_PROTO_CMD_SIZE + FDFS_PROTO_ERR_NO_SIZE - 1] = errno;
        return header;
    }
}
