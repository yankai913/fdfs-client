package com.zoo.fdfs.common;

import static com.zoo.fdfs.api.Constants.FDFS_GROUP_NAME_MAX_LEN;
import static com.zoo.fdfs.api.Constants.TRACKER_PROTO_CMD_RESP;

import java.io.InputStream;

import com.zoo.fdfs.api.Constants;


/**
 * 
 * @author yankai913@gmail.com
 * @date 2014-3-13
 */
public class Messages {
            
    public static byte[] createRequest(String groupName, String charsetName) throws Exception {
        byte[] groupNameByteArr = null;
        byte cmd = 0;
        byte errorNo = 0;
        int bodyLength = 0;
        if (Strings.isBlank(groupName)) {
            cmd = Constants.TRACKER_PROTO_CMD_SERVICE_QUERY_STORE_WITHOUT_GROUP_ONE;
            bodyLength = 0;
        } else {
            cmd = Constants.TRACKER_PROTO_CMD_SERVICE_QUERY_STORE_WITH_GROUP_ONE;
            bodyLength = Constants.FDFS_GROUP_NAME_MAX_LEN;
            groupNameByteArr = groupName.getBytes(charsetName);
        }
        int headerLenght = 10;
        int totalLength = headerLenght + bodyLength;
        WriteByteArrayFragment request = new WriteByteArrayFragment(totalLength);
        {
            request.writeLong(bodyLength);
            request.writeByte(cmd);
            request.writeByte(errorNo);
        }
        {
            if (Strings.isNotBlank(groupName)) {
                request.writeSubBytes(groupNameByteArr, FDFS_GROUP_NAME_MAX_LEN);
            }
        }
        return request.getData();
    }
    
    public static byte[] createRequest(String groupName, String fileName, String charsetName, byte cmd,
            byte errorNo) throws Exception {
        byte[] groupNameByteArr = groupName.getBytes(charsetName);
        byte[] fileNameByteArr = fileName.getBytes(charsetName);

        int headerLenght = 10;
        int bodyLength = FDFS_GROUP_NAME_MAX_LEN + fileNameByteArr.length;
        int totalLength = headerLenght + bodyLength;
        WriteByteArrayFragment request = new WriteByteArrayFragment(totalLength);
        // 填充header
        {
            /**
             * FDFS_PROTO_PKG_LEN_SIZE + FDFS_PROTO_CMD_SIZE +
             * FDFS_PROTO_ERR_NO_SIZE = 10
             * 
             * head有10个字节，8个字节存消息体的长度，1个字节的cmd，1个字节的errno。
             * 
             * |---8bytes(bodyLength)---|---1byte(cmd)---|---1byte(errno)---|
             * 
             * */
            request.writeLong(bodyLength);
            request.writeByte(cmd);
            request.writeByte(errorNo);

        }
        // 填充body
        {
            request.writeSubBytes(groupNameByteArr, FDFS_GROUP_NAME_MAX_LEN);
            request.writeBytes(fileNameByteArr);
        }
        return request.getData();
    }


    public static byte[] readResponse(InputStream is) throws Exception {
        byte[] respHeader = new byte[10];
        int length = is.read(respHeader);
        // 判断读取的header长度
        if (length != respHeader.length) {
            throw new IllegalStateException("read responseHeader fail, length not enouth, length: " + length);
        }
        // 判断相应的标志位，为响应标志
        if (respHeader[8] != TRACKER_PROTO_CMD_RESP) {
            throw new IllegalStateException("invalid responseHeader cmd, cmd: " + respHeader[8]);
        }
        // 异常标志位，0是正常，非0异常。
        if (respHeader[9] != 0) {
            throw new IllegalStateException("invalid errorNo , errorNo: " + respHeader[9]);
        }
        // 判断body的长度。
        long bodyLength = Bytes.bytes2long(respHeader, 0);
        if (bodyLength < 0) {
            throw new IllegalStateException("invalid bodyLength, bodyLength: " + bodyLength);
        }
        byte[] body = new byte[(int) bodyLength];
        int readBodyLength = is.read(body);
        // 验证body读完整。
        if (bodyLength != readBodyLength) {
            throw new IllegalStateException("invalid read body , bodyLength: " + bodyLength
                    + ", readBodyLength: " + readBodyLength);
        }
        return body;
    }
}
