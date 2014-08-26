package com.zoo.fdfs.support.client.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zoo.fdfs.api.Connection;
import com.zoo.fdfs.api.Constants;
import com.zoo.fdfs.api.FdfsClientConfig;
import com.zoo.fdfs.api.FdfsException;
import com.zoo.fdfs.api.StorageConfig;
import com.zoo.fdfs.api.TrackerClient;
import com.zoo.fdfs.api.UploadCallback;
import com.zoo.fdfs.common.Asserts;
import com.zoo.fdfs.common.Bytes;
import com.zoo.fdfs.common.Circle;
import com.zoo.fdfs.common.IOs;
import com.zoo.fdfs.common.Messages;
import com.zoo.fdfs.common.ReadByteArrayFragment;
import com.zoo.fdfs.common.ResponseBody;
import com.zoo.fdfs.common.Strings;
import com.zoo.fdfs.common.WriteByteArrayFragment;
import com.zoo.fdfs.support.SimpleConnectionPool;
import com.zoo.fdfs.support.callback.UploadStreamCallback;
import com.zoo.fdfs.support.client.UploadClient;


/**
 * 
 * @author yankai913@gmail.com
 * @date 2014-8-25
 */
public class DefaultUploadClient extends AbstractClient implements UploadClient {

    private static final Logger logger = LoggerFactory.getLogger(DefaultUploadClient.class);

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


    private String[] doUploadFile(String groupName, String localFileName, String fileExtName, Map<String, String> meta) throws FdfsException {
        byte cmd = Constants.STORAGE_PROTO_CMD_UPLOAD_FILE;
        return doUploadFile(cmd, groupName, localFileName, fileExtName, meta);
    }


    private String[] doUploadFile(byte cmd, String groupName, String localFileName, String fileExtName, Map<String, String> meta) throws FdfsException {
        File file = IOs.getFile(localFileName);
        FileInputStream fis = IOs.getFileInputStream(file);
        if (Strings.isBlank(fileExtName)) {
            int idx = localFileName.lastIndexOf(".");
            if (idx > 0 && (localFileName.length() - idx <= Constants.FDFS_FILE_EXT_NAME_MAX_LEN + 1)) {
                fileExtName = localFileName.substring(idx + 1);
            }
        }
        String masterFileName = null;
        String prefixName = null;
        return this.doUploadFile(cmd, groupName, masterFileName, prefixName, fileExtName, file.length(), new UploadStreamCallback(fis, file.length()), meta);
    }


    private String[] doUploadFile(byte cmd, String groupName, String masterFileName, String prefixName, String fileExtName, long fileSize, UploadCallback callback, Map<String, String> meta)
            throws FdfsException {
        byte[] fileExtNameByteArr = Strings.getBytes(fileExtName, getFdfsClientConfig().getCharset());
        fileExtNameByteArr = Bytes.wrap(fileExtNameByteArr, Constants.FDFS_FILE_EXT_NAME_MAX_LEN);
        boolean uploadSlave = (Strings.isNotBlank(groupName)) && (Strings.isNotBlank(masterFileName)) && (prefixName != null);
        byte[] sizeBytes = null;
        long bodyLength = 0L;
        Connection con = null;
        int offset = 0;
        byte[] masterFileNameByteArr = null;
        // update: masterFileName, filePrefixName
        // save: fileExtName
        if (uploadSlave) {// update
            masterFileNameByteArr = Strings.getBytes(masterFileName, getFdfsClientConfig().getCharset());
            con = getUpdatableConnection(groupName, masterFileName);
            sizeBytes = new byte[2 * Constants.FDFS_PROTO_PKG_LEN_SIZE];
            bodyLength = sizeBytes.length + Constants.FDFS_FILE_PREFIX_MAX_LEN + Constants.FDFS_FILE_EXT_NAME_MAX_LEN + masterFileNameByteArr.length + fileSize;
            byte[] masterFileNameLengthByteArr = new byte[8];
            Bytes.long2bytes(masterFileName.length(), masterFileNameLengthByteArr);
            System.arraycopy(masterFileNameLengthByteArr, 0, sizeBytes, 0, masterFileNameLengthByteArr.length);
            offset = masterFileNameLengthByteArr.length;
        }
        else {// save
            con = getWritableConnection(groupName);
            sizeBytes = new byte[1 * Constants.FDFS_PROTO_PKG_LEN_SIZE + 1];
            bodyLength = sizeBytes.length + Constants.FDFS_FILE_EXT_NAME_MAX_LEN + fileSize;
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
            filePrefixNameByteArr = Bytes.wrap(filePrefixNameByteArr, Constants.FDFS_FILE_EXT_NAME_MAX_LEN);
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
            responseBody = Messages.readResponse(is, Constants.TRACKER_QUERY_STORAGE_STORE_BODY_LEN);
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
        if (responseBody.getBody().length <= Constants.FDFS_GROUP_NAME_MAX_LEN) {
            throw new FdfsException("responseBody.length <=" + Constants.FDFS_GROUP_NAME_MAX_LEN);
        }
        //read
        ReadByteArrayFragment response = new ReadByteArrayFragment(responseBody.getBody()); 
        String newGroupName = response.readString(0, Constants.FDFS_GROUP_NAME_MAX_LEN);
        String newRemoteFileName = response.readString(0, responseBody.getBody().length - Constants.FDFS_GROUP_NAME_MAX_LEN);
        String[] result = new String[2];
        result[0] = newGroupName;
        result[1] = newRemoteFileName;
        if (meta == null || meta.isEmpty()) {
            return result;
        }
        //setMeta
        
        return null;
    }


    private Connection getWritableConnection(String groupName) throws FdfsException {
        if (getWritableConnectionPool() == null) {
            logger.debug("open getWritableConnection...");
            Set<StorageConfig> writableStorageConfigSet = getTrackerClient().getStoreStorageSet(groupName);
            
            Asserts.assertCollectionIsBlank(writableStorageConfigSet, "writableStorageConfigSet is empty");
            
            Circle<StorageConfig> addressCircle = new Circle<StorageConfig>(writableStorageConfigSet);
            final SimpleConnectionPool connectionPool = new SimpleConnectionPool(getFdfsClientConfig().getFetchPoolSize());
            for (int i = 0; i <= connectionPool.getElementLength(); i++) {
                StorageConfig storageConfig = addressCircle.readNext();
                InetSocketAddress inetSocketAddress = storageConfig.getInetSocketAddress();
                byte storePathIndex = storageConfig.getStorePathIndex();
                try {
                    Connection con = super.newConnection(inetSocketAddress);
                    con.setStorePathIndex(storePathIndex);
                    connectionPool.put(con);
                } catch (Exception e) {
                    throw new FdfsException("newConnection fail, inetSocketAddress=[" + inetSocketAddress + "]" + e.getMessage(), e);
                }
            }
            if (!connectionPool.isFull()) {
                throw new FdfsException("init ConnectionPool fail...");
            }
            setWritableConnectionPool(connectionPool);
        }
        return getWritableConnectionPool().get();
    }


    @Override
    public String[] uploadFile(String localFileName, String fileExtName, Map<String, String> meta) throws FdfsException {
        checkBeforeUpload(localFileName, fileExtName);
        String groupName = null;
        return doUploadFile(groupName, localFileName, fileExtName, meta);
    }


    @Override
    public String[] uploadFile(byte[] fileBuff, int offset, int length, String fileExtName, Map<String, String> meta) throws FdfsException {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public String[] uploadFile(String groupName, byte[] fileBuff, int offset, int length, String fileExtName, Map<String, String> meta) throws FdfsException {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public String[] uploadFile(byte[] fileBuff, String fileExtName, Map<String, String> meta) throws FdfsException {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public String[] uploadFile(String groupName, byte[] fileBuff, String fileExtName, Map<String, String> meta) throws FdfsException {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public String[] uploadFile(String groupName, long fileSize, UploadCallback callback, String fileExtName, Map<String, String> meta) throws FdfsException {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public String[] uploadFile(String groupName, String masterFileName, String prefixName, String localFileName, String fileExtName, Map<String, String> meta) throws FdfsException {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public String[] uploadFile(String groupName, String masterFileName, String prefixName, byte[] fileBuff, String fileExtName, Map<String, String> meta) throws FdfsException {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public String[] uploadFile(String groupName, String masterFileName, String prefixName, byte[] fileBuff, int offset, int length, String fileExtName, Map<String, String> meta) throws FdfsException {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public String[] uploadFile(String groupName, String masterFileName, String prefixName, long fileSize, UploadCallback callback, String fileExtName, Map<String, String> meta) throws FdfsException {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public String[] uploadAppenderFile(String localFileName, String fileExtName, Map<String, String> meta) throws FdfsException {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public String[] uploadAppenderFile(byte[] fileBuff, int offset, int length, String fileExtName, Map<String, String> meta) throws FdfsException {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public String[] uploadAppenderFile(String groupName, byte[] fileBuff, int offset, int length, String fileExtName, Map<String, String> meta) throws FdfsException {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public String[] uploadAppenderFile(byte[] fileBuff, String fileExtName, Map<String, String> meta) throws FdfsException {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public String[] uploadAppenderFile(String groupName, byte[] fileBuff, String fileExtName, Map<String, String> meta) throws FdfsException {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public String[] uploadAppenderFile(String groupName, long fileSize, UploadCallback callback, String fileExtName, Map<String, String> meta) throws FdfsException {
        // TODO Auto-generated method stub
        return null;
    }

}
