package com.zoo.fdfs.common;

import static com.zoo.fdfs.api.Constants.FDFS_GROUP_NAME_MAX_LEN;
import static com.zoo.fdfs.api.Constants.TRACKER_PROTO_CMD_RESP;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;

import com.zoo.fdfs.api.BaseStat;
import com.zoo.fdfs.api.Constants;
import com.zoo.fdfs.api.FdfsException;


/**
 * 
 * @author yankai913@gmail.com
 * @date 2014-3-13
 */
public class Messages {

    /**
     * 
     * @param groupName
     *            allow groupName is null
     * @param charsetName
     * @param cmd
     * @param errorNo
     * @return
     * @throws FdfsException
     */
    public static byte[] createRequest(String groupName, String charsetName, byte cmd, byte errorNo)
            throws FdfsException {
        byte[] groupNameByteArr = null;
        int headerLength = 10;
        int bodyLength = 0;
        if (Strings.isNotBlank(groupName)) {//
            bodyLength = FDFS_GROUP_NAME_MAX_LEN;
            groupNameByteArr = Strings.getBytes(groupName, charsetName);
        }
        int totalLength = headerLength + bodyLength;
        WriteByteArrayFragment request = new WriteByteArrayFragment(totalLength);
        // writeHeader
        {
            request.writeLong(bodyLength);
            request.writeByte(cmd);
            request.writeByte(errorNo);
        }
        // writeBody
        {
            if (Strings.isNotBlank(groupName)) {//
                request.writeLimitedBytes(groupNameByteArr, FDFS_GROUP_NAME_MAX_LEN);
            }
        }
        return request.getData();
    }


    public static byte[] createRequest(String groupName, String fileName, String charsetName, byte cmd,
            byte errorNo) throws FdfsException {
        byte[] groupNameByteArr = Strings.getBytes(groupName, charsetName);
        byte[] fileNameByteArr = Strings.getBytes(fileName, charsetName);

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
            request.writeLimitedBytes(groupNameByteArr, FDFS_GROUP_NAME_MAX_LEN);
            request.writeBytes(fileNameByteArr);
        }
        return request.getData();
    }


    public static ResponseBody readResponse(InputStream is, int expectBodyLenth) throws Exception {
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
            return new ResponseBody(respHeader[9], null);
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
        if (expectBodyLenth > 0 && readBodyLength != expectBodyLenth) {
            throw new IllegalStateException("readBodyLength:" + readBodyLength + ", expectBodyLenth:"
                    + expectBodyLenth);
        }
        return new ResponseBody(respHeader[9], body);
    }


    public static byte[] createHeader(long bodyLength, byte cmd, byte errorNo) {
        WriteByteArrayFragment header = new WriteByteArrayFragment(10);
        header.writeLong(bodyLength);
        header.writeByte(cmd);
        header.writeByte(errorNo);
        return header.getData();
    }


    public static byte[] createHeader(long bodyLength, byte cmd) {
        WriteByteArrayFragment header = new WriteByteArrayFragment(10);
        header.writeLong(bodyLength);
        header.writeByte(cmd);
        byte errorNo = 0;
        header.writeByte(errorNo);
        return header.getData();
    }


    @SuppressWarnings("unchecked")
    public static <T extends BaseStat> T[] decode(byte[] bodyBytes, Class<T> clazz, int fieldsTotalSize,
            String charset) {
        if (bodyBytes.length % fieldsTotalSize != 0) {
            throw new IllegalStateException("invalid bodyBytes.length, bodyBytes.length=" + bodyBytes.length);
        }
        int count = bodyBytes.length / fieldsTotalSize;
        T[] results = (T[]) Array.newInstance(clazz, count);
        int offset = 0;
        for (int i = 0; i < results.length; i++) {
            try {
                Constructor<T> constructor = clazz.getConstructor(String.class);
                results[i] = constructor.newInstance(charset);
                results[i].setFields(bodyBytes, offset);
                offset += fieldsTotalSize;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return results;
    }


    public static ResponseBody readStorageResponse(InputStream is, int expectBodyLenth) throws Exception {
        byte[] respHeader = new byte[10];
        int length = is.read(respHeader);
        // 判断读取的header长度
        if (length != respHeader.length) {
            throw new IllegalStateException("read responseHeader fail, length not enouth, length: " + length);
        }
        // 判断相应的标志位
        if (respHeader[8] != Constants.STORAGE_PROTO_CMD_RESP) {
            throw new IllegalStateException("invalid responseHeader cmd, cmd: " + respHeader[8]);
        }
        // 异常标志位，0是正常，非0异常。
        if (respHeader[9] != 0) {
            return new ResponseBody(respHeader[9], null);
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
        if (expectBodyLenth > 0 && readBodyLength != expectBodyLenth) {
            throw new IllegalStateException("readBodyLength:" + readBodyLength + ", expectBodyLenth:"
                    + expectBodyLenth);
        }
        return new ResponseBody(respHeader[9], body);
    }
}
