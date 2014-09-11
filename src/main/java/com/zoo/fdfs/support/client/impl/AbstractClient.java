package com.zoo.fdfs.support.client.impl;

import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zoo.fdfs.api.Connection;
import com.zoo.fdfs.api.Constants;
import com.zoo.fdfs.api.FdfsClientConfig;
import com.zoo.fdfs.api.FdfsException;
import com.zoo.fdfs.api.StorageConfig;
import com.zoo.fdfs.api.TrackerClient;
import com.zoo.fdfs.common.Asserts;
import com.zoo.fdfs.common.Bytes;
import com.zoo.fdfs.common.Messages;
import com.zoo.fdfs.common.ResponseBody;
import com.zoo.fdfs.common.Strings;
import com.zoo.fdfs.common.WriteByteArrayFragment;
import com.zoo.fdfs.support.SimpleConnection;


/**
 * 
 * @author yankai913@gmail.com
 * @date 2014-8-21
 */
public abstract class AbstractClient {

    private static final Logger logger = LoggerFactory.getLogger(AbstractClient.class);

    private FdfsClientConfig fdfsClientConfig;

    private TrackerClient trackerClient;


    public AbstractClient(FdfsClientConfig fdfsClientConfig, TrackerClient trackerClient) {
        this.fdfsClientConfig = fdfsClientConfig;
        this.trackerClient = trackerClient;
    }


    protected FdfsClientConfig getFdfsClientConfig() {
        return fdfsClientConfig;
    }


    protected TrackerClient getTrackerClient() {
        return trackerClient;
    }


    protected Connection newConnection(InetSocketAddress inetSocketAddress) throws FdfsException {
        try {
            int readTimeout = getFdfsClientConfig().getReadTimeout();
            int connectTimeout = getFdfsClientConfig().getConnectTimeout();
            Connection con = new SimpleConnection(readTimeout);
            con.connect(inetSocketAddress, connectTimeout);
            return con;
        } catch (Exception e) {
            throw new FdfsException(e.getMessage(), e);
        }
    }


    protected void checkBefore(String groupName, String remoteFileName) {
        Asserts.assertStringIsBlank(groupName, "groupName is blank");
        Asserts.assertStringIsBlank(remoteFileName, "remoteFileName is blank");
        // log
        logger.debug("groupName=[{}]", groupName);
        logger.debug("remoteFileName=[{}]", remoteFileName);
    }


    protected Connection getUpdatableConnection(String groupName, String remoteFileName) throws FdfsException {
        logger.debug("open updatable connection...");
        StorageConfig updatableStorageConfig = getTrackerClient().getUpdateStorage(groupName, remoteFileName);
        Asserts.assertNull(updatableStorageConfig, "updatableStorageConfig is null");
        return newConnection(updatableStorageConfig.getInetSocketAddress());
    }


    // TODO connection maybe shared with upload
    public int setMetadata(String groupName, String remoteFileName, Map<String, String> meta, byte opFlag)
            throws FdfsException {
        checkBefore(groupName, remoteFileName);
        byte[] sizeBytes = new byte[2 * Constants.FDFS_PROTO_PKG_LEN_SIZE];
        // fileNameLength
        byte[] remoteFileNameByteArr = Strings.getBytes(remoteFileName, getFdfsClientConfig().getCharset());
        byte[] remoteFileNameByteArrLengthByteArr = new byte[8];
        Bytes.long2bytes(remoteFileNameByteArr.length, remoteFileNameByteArrLengthByteArr);
        System.arraycopy(remoteFileNameByteArrLengthByteArr, 0, sizeBytes, 0,
            remoteFileNameByteArrLengthByteArr.length);
        // metaData
        byte[] metaDataByteArr = new byte[0];
        if (meta != null && meta.size() > 0) {
            StringBuilder sb = new StringBuilder();
            int i = 0;
            for (Map.Entry<String, String> entry : meta.entrySet()) {
                if (i != 0) {
                    sb.append(Constants.FDFS_RECORD_SEPERATOR);
                }
                String key = entry.getKey();
                String value = entry.getValue();
                sb.append(key).append(Constants.FDFS_FIELD_SEPERATOR).append(value);
                i++;
            }
            metaDataByteArr = Strings.getBytes(sb.toString(), getFdfsClientConfig().getCharset());
        }
        byte[] metaDataLengthByteArr = new byte[8];
        Bytes.long2bytes(metaDataByteArr.length, metaDataLengthByteArr);
        System.arraycopy(metaDataLengthByteArr, 0, sizeBytes, 8, metaDataLengthByteArr.length);
        // groupName
        byte[] groupNameByteArr = Strings.getBytes(groupName, getFdfsClientConfig().getCharset());
        groupNameByteArr = Bytes.wrap(groupNameByteArr, Constants.FDFS_GROUP_NAME_MAX_LEN);

        byte cmd = Constants.STORAGE_PROTO_CMD_SET_METADATA;
        // +1 ==> opFlag , one byte
        long bodyLength =
                sizeBytes.length + 1 + groupNameByteArr.length + remoteFileNameByteArr.length
                        + metaDataByteArr.length;

        byte[] header = Messages.createHeader(bodyLength, cmd, (byte) 0);
        int wholeLength = (int) (bodyLength + header.length);
        WriteByteArrayFragment headerBodyData = new WriteByteArrayFragment(wholeLength);
        // fill header
        headerBodyData.writeBytes(header);
        // fill sizeBytes
        headerBodyData.writeBytes(sizeBytes);
        // fill opFlag
        headerBodyData.writeByte(opFlag);
        // fill groupName
        headerBodyData.writeBytes(groupNameByteArr);
        // fill remoteFileName
        headerBodyData.writeBytes(remoteFileNameByteArr);
        // fill meta
        if (metaDataByteArr.length > 0) {
            headerBodyData.writeBytes(metaDataByteArr);
        }
        // send
        ResponseBody responseBody = null;
        Connection con = getUpdatableConnection(groupName, remoteFileName);
        try {
            con.writeBytes(headerBodyData.getData());
            // recv
            InputStream is = con.getInputStream();
            responseBody = Messages.readResponse(is, 0);
        } catch (Exception e) {
            String msg = "groupName=[{}], remoteFileName=[{}], meta=[{}], opFlag=[{}]";
            msg = Strings.format(msg, groupName, remoteFileName, meta, opFlag);
            throw new FdfsException(msg + e.getMessage(), e);
        } finally {
            con.close();
        }
        return responseBody.getErrorNo();
    }


    protected byte[] buildSimpleRequest(byte cmd, String groupName, String remoteFileName)
            throws FdfsException {
        byte[] groupNameByte = Strings.getBytes(groupName, getFdfsClientConfig().getCharset());
        groupNameByte = Bytes.wrap(groupNameByte, Constants.FDFS_GROUP_NAME_MAX_LEN);
        byte[] remoteFileByte = Strings.getBytes(remoteFileName, getFdfsClientConfig().getCharset());
        long bodyLength = groupNameByte.length + remoteFileByte.length;
        byte[] header = Messages.createHeader(bodyLength, cmd, (byte) 0);
        int size = (int) (header.length + bodyLength);
        WriteByteArrayFragment request = new WriteByteArrayFragment(size);
        request.writeBytes(header);
        request.writeBytes(groupNameByte);
        request.writeBytes(remoteFileByte);
        return request.getData();
    }


    // TODO connection maybe shared with upload
    public int deleteFile(String groupName, String remoteFileName) throws FdfsException {
        byte cmd = Constants.STORAGE_PROTO_CMD_DELETE_FILE;
        byte[] request = buildSimpleRequest(cmd, groupName, remoteFileName);
        Connection con = getUpdatableConnection(groupName, remoteFileName);
        ResponseBody responseBody = null;
        try {
            // send
            con.writeBytes(request);
            // recv
            InputStream is = con.getInputStream();
            responseBody = Messages.readResponse(is, 0);
            return responseBody.getErrorNo();
        } catch (Exception e) {
            throw new FdfsException(e.getMessage(), e);
        }
    }
}
