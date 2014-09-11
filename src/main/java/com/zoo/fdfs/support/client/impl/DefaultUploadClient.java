package com.zoo.fdfs.support.client.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zoo.fdfs.api.Base64;
import com.zoo.fdfs.api.Connection;
import com.zoo.fdfs.api.FdfsClientConfig;
import com.zoo.fdfs.api.FdfsException;
import com.zoo.fdfs.api.FileInfo;
import com.zoo.fdfs.api.StorageConfig;
import com.zoo.fdfs.api.TrackerClient;
import com.zoo.fdfs.api.UploadCallback;
import com.zoo.fdfs.common.Asserts;
import com.zoo.fdfs.common.Bytes;
import com.zoo.fdfs.common.IOs;
import com.zoo.fdfs.common.Messages;
import com.zoo.fdfs.common.ReadByteArrayFragment;
import com.zoo.fdfs.common.ResponseBody;
import com.zoo.fdfs.common.Strings;
import com.zoo.fdfs.common.WriteByteArrayFragment;
import com.zoo.fdfs.support.SimpleConnectionPool;
import com.zoo.fdfs.support.callback.UploadBufferCallback;
import com.zoo.fdfs.support.callback.UploadStreamCallback;
import com.zoo.fdfs.support.client.UploadClient;


/**
 * 
 * @author yankai913@gmail.com
 * @date 2014-8-25
 */
public class DefaultUploadClient extends AbstractClient implements UploadClient {

    private static final Logger logger = LoggerFactory.getLogger(DefaultUploadClient.class);

    public final static Base64 base64 = new Base64('-', '_', '.', 0);

    private SimpleConnectionPool writableConnectionPool;


    public DefaultUploadClient(FdfsClientConfig fdfsClientConfig, TrackerClient trackerClient) {
        super(fdfsClientConfig, trackerClient);
    }


    private void checkBeforeUpload(String localFileName, String fileExtName) {
        Asserts.assertStringIsBlank(localFileName, "localFileName is blank");
        Asserts.assertStringIsBlank(fileExtName, "fileExtName is blank");
        // log
        logger.debug("localFileName=[{}]", localFileName);
        logger.debug("fileExtName=[{}]", fileExtName);
    }


    private void setWritableConnectionPool(SimpleConnectionPool writableConnectionPool) {
        this.writableConnectionPool = writableConnectionPool;
    }


    private SimpleConnectionPool getWritableConnectionPool() {
        return writableConnectionPool;
    }


    private String[] doUploadFile(String groupName, String localFileName, String fileExtName,
            Map<String, String> meta) throws FdfsException {
        byte cmd = STORAGE_PROTO_CMD_UPLOAD_FILE;
        return doUploadFile(cmd, groupName, localFileName, fileExtName, meta);
    }


    private String[] doUploadFile(byte cmd, String groupName, String localFileName, String fileExtName,
            Map<String, String> meta) throws FdfsException {
        File file = IOs.getFile(localFileName);
        FileInputStream fis = IOs.getFileInputStream(file);
        if (Strings.isBlank(fileExtName)) {
            int idx = localFileName.lastIndexOf(".");
            if (idx > 0 && (localFileName.length() - idx <= FDFS_FILE_EXT_NAME_MAX_LEN + 1)) {
                fileExtName = localFileName.substring(idx + 1);
            }
        }
        String masterFileName = null;
        String prefixName = null;
        return this.doUploadFile(cmd, groupName, masterFileName, prefixName, fileExtName, file.length(),
            new UploadStreamCallback(fis, file.length()), meta);
    }


    private String[] doUploadFile(byte cmd, String groupName, String masterFileName, String prefixName,
            String fileExtName, long fileSize, UploadCallback callback, Map<String, String> meta)
            throws FdfsException {
        /**
         * protocol of save:
         * 
         * |--10bytes (header)--|
         * |--1bytes(storePathIndex)-8bytes(fileSizeBytes)--|
         * |--6bytes(extNameLength)--|
         * |--fileData--|
         */
        byte[] fileExtNameByteArr = Strings.getBytes(fileExtName, getFdfsClientConfig().getCharset());
        fileExtNameByteArr = Bytes.wrap(fileExtNameByteArr, FDFS_FILE_EXT_NAME_MAX_LEN);
        boolean uploadSlave =
                (Strings.isNotBlank(groupName)) && (Strings.isNotBlank(masterFileName))
                        && (prefixName != null);
        byte[] sizeBytes = null;
        long bodyLength = 0L;
        Connection con = null;
        int offset = 0;
        byte[] masterFileNameByteArr = null;
        // update: masterFileName, filePrefixName
        // save: fileExtName
        if (uploadSlave) {// update
            masterFileNameByteArr = Strings.getBytes(masterFileName, getFdfsClientConfig().getCharset());
            //
            con = getUpdatableConnection(groupName, masterFileName);
            sizeBytes = new byte[2 * FDFS_PROTO_PKG_LEN_SIZE];
            bodyLength =
                    sizeBytes.length + FDFS_FILE_PREFIX_MAX_LEN + FDFS_FILE_EXT_NAME_MAX_LEN
                            + masterFileNameByteArr.length + fileSize;
            byte[] masterFileNameLengthByteArr = new byte[8];
            Bytes.long2bytes(masterFileName.length(), masterFileNameLengthByteArr);
            System
                .arraycopy(masterFileNameLengthByteArr, 0, sizeBytes, 0, masterFileNameLengthByteArr.length);
            offset = masterFileNameLengthByteArr.length;
        }
        else {// save
            con = getWritableConnection(groupName);
            sizeBytes = new byte[1 * FDFS_PROTO_PKG_LEN_SIZE + 1];
            bodyLength = sizeBytes.length + FDFS_FILE_EXT_NAME_MAX_LEN + fileSize;
            sizeBytes[0] = con.getStorePathIndex();
            offset = 1;
        }
        // fileSize ===> byte[], copy fileData offset 1;
        byte[] fileSizeByteArr = new byte[8];
        Bytes.long2bytes(fileSize, fileSizeByteArr);
        System.arraycopy(fileSizeByteArr, 0, sizeBytes, offset, fileSizeByteArr.length);
        // header
        byte[] header = Messages.createHeader(bodyLength, cmd, (byte) 0);
        // whole package
        int requestLength = (int) (header.length + bodyLength - fileSize);
        WriteByteArrayFragment request = new WriteByteArrayFragment(requestLength);
        // fill header
        request.writeBytes(header);
        // fill header body length
        request.writeBytes(sizeBytes);
        // fill filePrefixName
        if (uploadSlave) {
            byte[] filePrefixNameByteArr = Strings.getBytes(prefixName, getFdfsClientConfig().getCharset());
            filePrefixNameByteArr = Bytes.wrap(filePrefixNameByteArr, FDFS_FILE_EXT_NAME_MAX_LEN);
            //
            request.writeBytes(filePrefixNameByteArr);
        }
        // fill fileExtName
        request.writeBytes(fileExtNameByteArr);
        // fill masterFileName
        if (uploadSlave) {
            request.writeBytes(masterFileNameByteArr);
        }
        // send
        OutputStream out = null;
        InputStream is = null;
        ResponseBody responseBody = null;
        try {
            out = con.getOutputStream();
            out.write(request.getData());
            // callback to send fileData
            byte errorNo = (byte) callback.send(out);
            if (errorNo != 0) {
                return null;
            }
            // receive
            is = con.getInputStream();
            responseBody = Messages.readResponse(is, -1);
        } catch (Exception e) {
            throw new FdfsException(e.getMessage(), e);
        } finally {
            con.close();
        }
        if (responseBody.getErrorNo() != 0) {
            logger.error("errorNo=[{}]", responseBody.getErrorNo());
            return null;
        }
        //
        if (responseBody.getBody().length <= FDFS_GROUP_NAME_MAX_LEN) {
            throw new FdfsException("responseBody.length <=" + FDFS_GROUP_NAME_MAX_LEN);
        }
        // read
        ReadByteArrayFragment response = new ReadByteArrayFragment(responseBody.getBody());
        String newGroupName = response.readString(0, FDFS_GROUP_NAME_MAX_LEN).trim();
        String newRemoteFileName =
                response.readString(0, responseBody.getBody().length - FDFS_GROUP_NAME_MAX_LEN).trim();
        String[] result = new String[2];
        result[0] = newGroupName;
        result[1] = newRemoteFileName;
        if (meta == null || meta.isEmpty()) {
            return result;
        }
        // setMeta
        byte opFlag = STORAGE_SET_METADATA_FLAG_OVERWRITE;
        int rs = 0;
        try {
            rs = super.setMetadata(newGroupName, newRemoteFileName, meta, opFlag);
            if (rs == 0) {
                return result;
            }
        } catch (Exception e) {
            rs = 5;
            throw new FdfsException(e.getMessage(), e);
        } finally {
            // delete file
            if (rs != 0) {
                super.deleteFile(newGroupName, newRemoteFileName);
            }
        }
        return null;
    }


    private Connection getWritableConnection(String groupName) throws FdfsException {
        if (getWritableConnectionPool() == null) {
            logger.debug("open getWritableConnection...");
            Set<StorageConfig> writableStorageConfigSet = getTrackerClient().getStoreStorageSet(groupName);

            Asserts.assertCollectionIsBlank(writableStorageConfigSet, "writableStorageConfigSet is empty");

            int perSize = getFdfsClientConfig().getStorageSizePerAddr();
            int elementSize = perSize * writableStorageConfigSet.size();
            final SimpleConnectionPool connectionPool = new SimpleConnectionPool(elementSize, 3);
            for (StorageConfig storageConfig : writableStorageConfigSet) {
                InetSocketAddress inetSocketAddress = storageConfig.getInetSocketAddress();
                byte storePathIndex = storageConfig.getStorePathIndex();
                for (int i = 0; i < perSize; i++) {
                    try {
                        Connection con = super.newConnection(inetSocketAddress);
                        con.setStorePathIndex(storePathIndex);
                        connectionPool.put(con);
                    } catch (Exception e) {
                        throw new FdfsException("newConnection fail, inetSocketAddress=[" + inetSocketAddress
                                + "]" + e.getMessage(), e);
                    }
                }
            }
            setWritableConnectionPool(connectionPool);
        }
        return getWritableConnectionPool().get();
    }


    @Override
    public String[] uploadFile(String localFileName, String fileExtName, Map<String, String> meta)
            throws FdfsException {
        checkBeforeUpload(localFileName, fileExtName);
        String groupName = null;
        return doUploadFile(groupName, localFileName, fileExtName, meta);
    }


    @Override
    public String[] uploadFile(byte[] fileBuff, int offset, int length, String fileExtName,
            Map<String, String> meta) throws FdfsException {
        String groupName = null;
        return uploadFile(groupName, fileBuff, offset, length, fileExtName, meta);
    }


    @Override
    public String[] uploadFile(String groupName, byte[] fileBuff, int offset, int length, String fileExtName,
            Map<String, String> meta) throws FdfsException {
        return this.doUploadFile(STORAGE_PROTO_CMD_UPLOAD_FILE, groupName, null, null, fileExtName, length,
            new UploadBufferCallback(fileBuff, offset, length), meta);
    }


    @Override
    public String[] uploadFile(byte[] fileBuff, String fileExtName, Map<String, String> meta)
            throws FdfsException {
        String group_name = null;
        return this.uploadFile(group_name, fileBuff, 0, fileBuff.length, fileExtName, meta);
    }


    @Override
    public String[] uploadFile(String groupName, byte[] fileBuff, String fileExtName, Map<String, String> meta)
            throws FdfsException {
        return this.doUploadFile(STORAGE_PROTO_CMD_UPLOAD_FILE, groupName, null, null, fileExtName,
            fileBuff.length, new UploadBufferCallback(fileBuff, 0, fileBuff.length), meta);
    }


    @Override
    public String[] uploadFile(String groupName, long fileSize, UploadCallback callback, String fileExtName,
            Map<String, String> meta) throws FdfsException {
        return this.doUploadFile(STORAGE_PROTO_CMD_UPLOAD_FILE, groupName, null, null, fileExtName, fileSize,
            callback, meta);
    }


    @Override
    public String[] uploadFile(String groupName, String masterFileName, String prefixName,
            String localFileName, String fileExtName, Map<String, String> meta) throws FdfsException {
        if (Strings.isBlank(groupName) || Strings.isBlank(masterFileName) || prefixName == null) {
            throw new IllegalArgumentException("invalid arguement");
        }
        File file = IOs.getFile(localFileName);
        FileInputStream fis = IOs.getFileInputStream(file);
        if (Strings.isBlank(fileExtName)) {
            int idx = localFileName.lastIndexOf(".");
            if (idx > 0 && (localFileName.length() - idx <= FDFS_FILE_EXT_NAME_MAX_LEN + 1)) {
                fileExtName = localFileName.substring(idx + 1);
            }
        }
        byte cmd = STORAGE_PROTO_CMD_UPLOAD_SLAVE_FILE;
        return this.doUploadFile(cmd, groupName, masterFileName, prefixName, fileExtName, file.length(),
            new UploadStreamCallback(fis, file.length()), meta);
    }


    @Override
    public String[] uploadFile(String groupName, String masterFileName, String prefixName, byte[] fileBuff,
            String fileExtName, Map<String, String> meta) throws FdfsException {
        if (Strings.isBlank(groupName) || Strings.isBlank(masterFileName) || prefixName == null) {
            throw new IllegalArgumentException("invalid arguement");
        }
        byte cmd = STORAGE_PROTO_CMD_UPLOAD_SLAVE_FILE;
        return this.doUploadFile(cmd, groupName, masterFileName, prefixName, fileExtName, fileBuff.length,
            new UploadBufferCallback(fileBuff, 0, fileBuff.length), meta);
    }


    @Override
    public String[] uploadFile(String groupName, String masterFileName, String prefixName, byte[] fileBuff,
            int offset, int length, String fileExtName, Map<String, String> meta) throws FdfsException {
        if (Strings.isBlank(groupName) || Strings.isBlank(masterFileName) || prefixName == null) {
            throw new IllegalArgumentException("invalid arguement");
        }
        byte cmd = STORAGE_PROTO_CMD_UPLOAD_SLAVE_FILE;
        return this.doUploadFile(cmd, groupName, masterFileName, prefixName, fileExtName, length,
            new UploadBufferCallback(fileBuff, offset, length), meta);
    }


    @Override
    public String[] uploadFile(String groupName, String masterFileName, String prefixName, long fileSize,
            UploadCallback callback, String fileExtName, Map<String, String> meta) throws FdfsException {
        return this.doUploadFile(STORAGE_PROTO_CMD_UPLOAD_SLAVE_FILE, groupName, masterFileName, prefixName,
            fileExtName, fileSize, callback, meta);
    }


    @Override
    public String[] uploadAppenderFile(String localFileName, String fileExtName, Map<String, String> meta)
            throws FdfsException {
        String groupName = null;
        byte cmd = STORAGE_PROTO_CMD_UPLOAD_APPENDER_FILE;
        return this.doUploadFile(cmd, groupName, localFileName, fileExtName, meta);

    }


    @Override
    public String[] uploadAppenderFile(byte[] fileBuff, int offset, int length, String fileExtName,
            Map<String, String> meta) throws FdfsException {
        String groupName = null;
        return this.uploadAppenderFile(groupName, fileBuff, offset, length, fileExtName, meta);
    }


    @Override
    public String[] uploadAppenderFile(String groupName, byte[] fileBuff, int offset, int length,
            String fileExtName, Map<String, String> meta) throws FdfsException {
        return this.doUploadFile(STORAGE_PROTO_CMD_UPLOAD_APPENDER_FILE, groupName, null, null, fileExtName,
            length, new UploadBufferCallback(fileBuff, offset, length), meta);
    }


    @Override
    public String[] uploadAppenderFile(byte[] fileBuff, String fileExtName, Map<String, String> meta)
            throws FdfsException {
        String groupName = null;
        return this.uploadAppenderFile(groupName, fileBuff, 0, fileBuff.length, fileExtName, meta);
    }


    @Override
    public String[] uploadAppenderFile(String groupName, byte[] fileBuff, String fileExtName,
            Map<String, String> meta) throws FdfsException {
        return this.doUploadFile(STORAGE_PROTO_CMD_UPLOAD_APPENDER_FILE, groupName, null, null, fileExtName,
            fileBuff.length, new UploadBufferCallback(fileBuff, 0, fileBuff.length), meta);
    }


    @Override
    public String[] uploadAppenderFile(String groupName, long fileSize, UploadCallback callback,
            String fileExtName, Map<String, String> meta) throws FdfsException {
        return this.doUploadFile(STORAGE_PROTO_CMD_UPLOAD_APPENDER_FILE, groupName, null, null, fileExtName,
            fileSize, callback, meta);
    }


    @Override
    public int appendFile(String groupName, String appenderFileName, String localFileName)
            throws FdfsException {
        File file = IOs.getFile(localFileName);
        FileInputStream fis = IOs.getFileInputStream(file);
        return this.doAppendFile(groupName, appenderFileName, file.length(), new UploadStreamCallback(fis,
            file.length()));
    }


    @Override
    public int appendFile(String groupName, String appenderFileName, byte[] fileBuff) throws FdfsException {
        return this.doAppendFile(groupName, appenderFileName, fileBuff.length, new UploadBufferCallback(
            fileBuff, 0, fileBuff.length));
    }


    @Override
    public int appendFile(String groupName, String appenderFileName, byte[] fileBuff, int offset, int length)
            throws FdfsException {
        return this.doAppendFile(groupName, appenderFileName, fileBuff.length, new UploadBufferCallback(
            fileBuff, offset, length));
    }


    @Override
    public int appendFile(String groupName, String appenderFileName, long fileSize, UploadCallback callback)
            throws FdfsException {
        return this.doAppendFile(groupName, appenderFileName, fileSize, callback);
    }


    private int doAppendFile(String groupName, String appenderFileName, long fileSize, UploadCallback callback)
            throws FdfsException {
        if (Strings.isBlank(groupName) || Strings.isBlank(appenderFileName)) {
            return ERR_NO_EINVAL;
        }
        byte[] appenderFileNameByteArr =
                Strings.getBytes(appenderFileName, getFdfsClientConfig().getCharset());
        long bodyLength = 2 * FDFS_PROTO_PKG_LEN_SIZE + appenderFileNameByteArr.length + fileSize;
        byte[] header = Messages.createHeader(bodyLength, STORAGE_PROTO_CMD_APPEND_FILE);
        WriteByteArrayFragment request =
                new WriteByteArrayFragment((int) (header.length + bodyLength - fileSize));
        // fill header
        request.writeBytes(header);
        // fill appenderFileNameByteArr.length
        byte[] appenderFileNameByteArrLengthByteArr = new byte[8];
        Bytes.long2bytes(appenderFileNameByteArr.length, appenderFileNameByteArrLengthByteArr);
        request.writeBytes(appenderFileNameByteArrLengthByteArr);
        // fill fileSize
        byte[] fileSizeByteArr = new byte[8];
        Bytes.long2bytes(fileSize, fileSizeByteArr);
        request.writeBytes(fileSizeByteArr);
        // fill appenderFileNameByteArr
        request.writeBytes(appenderFileNameByteArr);
        // send
        Connection con = getUpdatableConnection(groupName, appenderFileName);
        InputStream is = null;
        OutputStream out = null;
        ResponseBody responseBody = null;
        int errorNo = 0;
        try {
            is = con.getInputStream();
            out = con.getOutputStream();
            out.write(request.getData());
            errorNo = callback.send(out);
            if (errorNo != 0) {
                return errorNo;
            }
            responseBody = Messages.readResponse(is, 0);
            if (responseBody.getErrorNo() != 0) {
                return responseBody.getErrorNo();
            }
            return 0;
        } catch (Exception e) {
            throw new FdfsException(e.getMessage(), e);
        } finally {
            con.close();
        }
    }


    @Override
    public int modifyFile(String groupName, String appenderFileName, long fileOffset, String localFileName)
            throws FdfsException {
        File file = IOs.getFile(localFileName);
        FileInputStream fis = IOs.getFileInputStream(file);
        return this.doModifyFile(groupName, appenderFileName, fileOffset, file.length(),
            new UploadStreamCallback(fis, file.length()));
    }


    @Override
    public int modifyFile(String groupName, String appenderFileName, long fileOffset, byte[] fileBuff)
            throws FdfsException {
        return this.doModifyFile(groupName, appenderFileName, fileOffset, fileBuff.length,
            new UploadBufferCallback(fileBuff, 0, fileBuff.length));
    }


    @Override
    public int modifyFile(String groupName, String appenderFileName, long fileOffset, byte[] fileBuff,
            int bufferOffset, int bufferLength) throws FdfsException {
        return this.doModifyFile(groupName, appenderFileName, fileOffset, fileBuff.length,
            new UploadBufferCallback(fileBuff, bufferOffset, fileBuff.length));
    }


    @Override
    public int modifyFile(String groupName, String appenderFileName, long fileOffset, long modifySize,
            UploadCallback callback) throws FdfsException {
        return this.doModifyFile(groupName, appenderFileName, fileOffset, modifySize, callback);
    }


    private int doModifyFile(String groupName, String appenderFileName, long fileOffset, long modifySize,
            UploadCallback callback) throws FdfsException {
        if (Strings.isBlank(groupName) || Strings.isBlank(appenderFileName)) {
            return ERR_NO_EINVAL;
        }
        byte[] appenderFileNameByteArr =
                Strings.getBytes(appenderFileName, getFdfsClientConfig().getCharset());
        long bodyLength = 3 * FDFS_PROTO_PKG_LEN_SIZE + appenderFileNameByteArr.length + modifySize;
        byte[] header = Messages.createHeader(bodyLength, STORAGE_PROTO_CMD_MODIFY_FILE);
        WriteByteArrayFragment request =
                new WriteByteArrayFragment((int) (header.length + bodyLength - modifySize));
        // fill header
        request.writeBytes(header);
        // fill appenderFileNameByteArr.length
        byte[] appenderFileNameByteArrLengthByteArr = new byte[8];
        Bytes.long2bytes(appenderFileNameByteArr.length, appenderFileNameByteArrLengthByteArr);
        request.writeBytes(appenderFileNameByteArrLengthByteArr);
        // fill fileOffset
        byte[] fileOffsetByteArr = new byte[8];
        Bytes.long2bytes(fileOffset, fileOffsetByteArr);
        request.writeBytes(fileOffsetByteArr);
        // fill modifySize
        byte[] modifySizeByteArr = new byte[8];
        Bytes.long2bytes(modifySize, modifySizeByteArr);
        request.writeBytes(modifySizeByteArr);
        // fill appenderFileNameByteArr
        request.writeBytes(appenderFileNameByteArr);
        // send
        Connection con = getUpdatableConnection(groupName, appenderFileName);
        InputStream is = null;
        OutputStream out = null;
        ResponseBody responseBody = null;
        int errorNo = 0;
        try {
            is = con.getInputStream();
            out = con.getOutputStream();
            out.write(request.getData());
            errorNo = callback.send(out);
            if (errorNo != 0) {
                return errorNo;
            }
            responseBody = Messages.readResponse(is, 0);
            if (responseBody.getErrorNo() != 0) {
                return responseBody.getErrorNo();
            }
            return 0;
        } catch (Exception e) {
            throw new FdfsException(e.getMessage(), e);
        } finally {
            con.close();
        }
    }


    @Override
    public int truncateFile(String groupName, String appenderFileName) throws FdfsException {
        long truncatedFileSize = 0;
        return this.truncateFile(groupName, appenderFileName, truncatedFileSize);
    }


    @Override
    public int truncateFile(String groupName, String appenderFileName, long truncatedFileSize)
            throws FdfsException {
        if (Strings.isBlank(groupName) || Strings.isBlank(appenderFileName)) {
            return ERR_NO_EINVAL;
        }
        byte[] appenderFileNameByteArr =
                Strings.getBytes(appenderFileName, getFdfsClientConfig().getCharset());
        long bodyLength = 2 * FDFS_PROTO_PKG_LEN_SIZE + appenderFileNameByteArr.length;
        byte[] header = Messages.createHeader(bodyLength, STORAGE_PROTO_CMD_TRUNCATE_FILE);
        WriteByteArrayFragment request = new WriteByteArrayFragment((int) (header.length + bodyLength));
        // fill header
        request.writeBytes(header);
        // fill appenderFileNameByteArr.length
        byte[] appenderFileNameByteArrLengthByteArr = new byte[8];
        Bytes.long2bytes(appenderFileNameByteArr.length, appenderFileNameByteArrLengthByteArr);
        request.writeBytes(appenderFileNameByteArrLengthByteArr);
        // fill truncatedFileSize
        byte[] truncatedFileSizeByteArr = new byte[8];
        Bytes.long2bytes(truncatedFileSize, truncatedFileSizeByteArr);
        request.writeBytes(truncatedFileSizeByteArr);
        // fill appenderFileNameByteArr
        request.writeBytes(appenderFileNameByteArr);
        // send
        Connection con = getUpdatableConnection(groupName, appenderFileName);
        InputStream is = null;
        OutputStream out = null;
        try {
            out = con.getOutputStream();
            out.write(request.getData());
            ResponseBody responseBody = Messages.readResponse(is, 0);
            if (responseBody.getErrorNo() != 0) {
                return responseBody.getErrorNo();
            }
            return 0;
        } catch (Exception e) {
            throw new FdfsException(e.getMessage(), e);
        }
    }


    @Override
    public FileInfo getFileInfo(String groupName, String remoteFileName) throws FdfsException {
        int fileNameLength =
                FDFS_FILE_PATH_LEN + FDFS_FILENAME_BASE64_LENGTH + FDFS_FILE_EXT_NAME_MAX_LEN + 1;
        if (remoteFileName.length() < fileNameLength) {
            return null;
        }
        byte[] buff =
                base64.decodeAuto(remoteFileName.substring(FDFS_FILE_PATH_LEN, FDFS_FILE_PATH_LEN
                        + FDFS_FILENAME_BASE64_LENGTH));

        long fileSize = Bytes.bytes2long(buff, 4 * 2);
        if (((remoteFileName.length() > TRUNK_LOGIC_FILENAME_LENGTH) || ((remoteFileName.length() > NORMAL_LOGIC_FILENAME_LENGTH) && ((fileSize & TRUNK_FILE_MARK_SIZE) == 0)))
                || ((fileSize & APPENDER_FILE_SIZE) != 0)) { // slave
                                                             // file
                                                             // or
                                                             // appender
                                                             // file
            FileInfo fileInfo = this.queryFileInfo(groupName, remoteFileName);
            return fileInfo;
        }

        FileInfo fileInfo = new FileInfo(fileSize, 0, 0, getIpAddress(buff, 0));
        fileInfo.setCreateTimestamp(new Timestamp(Bytes.bytes2int(buff, 4)));
        if ((fileSize >> 63) != 0) {
            fileSize &= 0xFFFFFFFFL; // low 32 bits is file size
            fileInfo.setFileSize(fileSize);
        }
        fileInfo.setCrc32(Bytes.bytes2int(buff, 4 * 4));
        return fileInfo;
    }


    private String getIpAddress(byte[] bs, int offset) {
        // storage server ID
        if (bs[0] == 0 || bs[3] == 0) {
            return "";
        }

        int n;
        StringBuilder sbResult = new StringBuilder(16);
        for (int i = offset; i < offset + 4; i++) {
            n = (bs[i] >= 0) ? bs[i] : 256 + bs[i];
            if (sbResult.length() > 0) {
                sbResult.append(".");
            }
            sbResult.append(String.valueOf(n));
        }
        return sbResult.toString();
    }


    @Override
    public FileInfo queryFileInfo(String groupName, String remoteFileName) throws FdfsException {
        byte cmd = STORAGE_PROTO_CMD_QUERY_FILE_INFO;
        Connection con = getUpdatableConnection(groupName, remoteFileName);
        byte[] request = buildSimpleRequest(cmd, groupName, remoteFileName);
        ResponseBody responseBody = null;
        try {
            // send
            con.writeBytes(request);
            // recv
            InputStream is = con.getInputStream();
            responseBody = Messages.readResponse(is, 3 * FDFS_PROTO_PKG_LEN_SIZE + FDFS_IPADDR_SIZE);
        } catch (Exception e) {
            throw new FdfsException(e.getMessage(), e);
        }
        if (responseBody.getErrorNo() != 0) {
            return null;
        }
        ReadByteArrayFragment response = new ReadByteArrayFragment(responseBody.getBody());
        long fileSize = response.readLong();
        long createTimestamp = response.readLong();
        int crc32 = (int) response.readLong();
        String sourceIpAddr = response.readString(FDFS_IPADDR_SIZE);
        return new FileInfo(fileSize, createTimestamp, crc32, sourceIpAddr);
    }


    @Override
    public Map<String, String> getMetadata(String groupName, String remoteFileName) throws FdfsException {
        byte cmd = STORAGE_PROTO_CMD_GET_METADATA;
        byte[] request = buildSimpleRequest(cmd, groupName, remoteFileName);
        ResponseBody responseBody = null;
        Connection con = getUpdatableConnection(groupName, remoteFileName);
        try {
            // send
            con.writeBytes(request);
            // recv
            InputStream is = con.getInputStream();
            responseBody = Messages.readResponse(is, -1);
            if (responseBody.getErrorNo() != 0) {
                return null;
            }
            String metaStr = new String(responseBody.getBody(), getFdfsClientConfig().getCharset());
            return splitMetadata(metaStr, FDFS_RECORD_SEPERATOR, FDFS_FIELD_SEPERATOR);
        } catch (Exception e) {
            throw new FdfsException(e.getMessage(), e);
        }
    }


    public static Map<String, String> splitMetadata(String metaStr, String recordSeperator,
            String filedSeperator) {
        String[] rows = metaStr.split(recordSeperator);
        Map<String, String> map = new HashMap<String, String>(rows.length);
        for (int i = 0; i < rows.length; i++) {
            String[] cols = rows[i].split(filedSeperator, 2);
            if (cols.length == 2) {
                map.put(cols[0], cols[1]);
            }
        }

        return map;
    }

}
