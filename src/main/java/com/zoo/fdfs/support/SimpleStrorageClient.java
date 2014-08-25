package com.zoo.fdfs.support;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.Set;

import static com.zoo.fdfs.api.Constants.FDFS_GROUP_NAME_MAX_LEN;
import static com.zoo.fdfs.api.Constants.STORAGE_PROTO_CMD_DOWNLOAD_FILE;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zoo.fdfs.api.Connection;
import com.zoo.fdfs.api.Constants;
import com.zoo.fdfs.api.FdfsClientConfig;
import com.zoo.fdfs.api.FdfsException;
import com.zoo.fdfs.api.FileInfo;
import com.zoo.fdfs.api.StorageClient;
import com.zoo.fdfs.api.StorageConfig;
import com.zoo.fdfs.api.TrackerClient;
import com.zoo.fdfs.common.Bytes;
import com.zoo.fdfs.common.Circle;
import com.zoo.fdfs.common.Collections;
import com.zoo.fdfs.common.IOs;
import com.zoo.fdfs.common.Messages;
import com.zoo.fdfs.common.ResponseBody;
import com.zoo.fdfs.common.Strings;
import com.zoo.fdfs.common.WriteByteArrayFragment;


/**
 * Prototype, Threadsafe
 * 
 * @author yankai913@gmail.com
 * @date 2014-4-3
 */
public class SimpleStrorageClient implements StorageClient {

    private static final Logger logger = LoggerFactory.getLogger(SimpleStrorageClient.class);

    private FdfsClientConfig fdfsClientConfig;

    private TrackerClient trackerClient;

    private SimpleConnectionPool fetchConnectionPool;

    private SimpleConnectionPool storageConnectionPool;

    private SimpleConnectionPool updateConnectionPool;

    private byte errorNo;
    

    public SimpleStrorageClient(TrackerClient trackerClient, FdfsClientConfig fdfsClientConfig) {
        this.fdfsClientConfig = fdfsClientConfig;
        this.trackerClient = trackerClient;
    }


    public TrackerClient getTrackerClient() {
        return trackerClient;
    }


    public FdfsClientConfig getFdfsClientConfig() {
        return fdfsClientConfig;
    }


    Connection newConnection(InetSocketAddress inetSocketAddress) throws Exception {
        int readTimeout = getFdfsClientConfig().getReadTimeout();
        int connectTimeout = getFdfsClientConfig().getConnectTimeout();
        Connection con = new SimpleConnection(readTimeout);
        con.connect(inetSocketAddress, connectTimeout);
        return con;
    }


    public Connection getFetchableConnection(String groupName, String remoteFileName) {
        if (Strings.isBlank(groupName) || Strings.isBlank(remoteFileName)) {
            throw new IllegalArgumentException("groupName, remoteFileName is blank");
        }
        Set<StorageConfig> storageConfigSet = trackerClient.getFetchStorageSet(groupName, remoteFileName);
        if (!Collections.isNotEmpty(storageConfigSet)) {
            throw new IllegalStateException("storageConfigSet is empty");
        }
        Circle<StorageConfig> addressCircle = new Circle<StorageConfig>(storageConfigSet);
        int fetchSize = getFdfsClientConfig().getFetchPoolSize();
        fetchConnectionPool = new SimpleConnectionPool(fetchSize);
        for (int i = 0; i <= fetchSize; i++) {
            InetSocketAddress inetSocketAddress = addressCircle.readNext().getInetSocketAddress();
            try {
                fetchConnectionPool.put(newConnection(inetSocketAddress));
            } catch (Exception e) {
                logger.error("newConnection fail, inetSocketAddress=[{}]", inetSocketAddress, e);
            }
        }
        int realFetchSize = fetchConnectionPool.getElementLength();
        if (realFetchSize != fetchSize) {
            throw new FdfsException("getFetchableConnection fail, realFetchSize=[" + realFetchSize
                    + "] <> fetchSize=[" + fetchSize + "]");
        }
        return fetchConnectionPool.get();
    }


    public Connection getUpdateableConnection(String groupName, String remoteFileName) {
        return null;
    }


    public Connection getWriteableConnection(String groupName) {
        return null;
    }


    @Override
    public String[] uploadFile(String localFileName, String fileExtName, Map<String, String> meta) {
        return null;
    }



    @Override
    public String[] uploadFile(byte[] fileBuff, int offset, int length, String fileExtName,
            Map<String, String> meta) {
        return null;
    }




    @Override
    public String[] uploadFile(byte[] fileBuff, String fileExtName, Map<String, String> meta) {
        // TODO Auto-generated method stub
        return null;
    }



    @Override
    public String[] uploadFile(String groupName, String masterFileName, String prefixName,
            String localFileName, String fileExtName, Map<String, String> meta) {
        if (Strings.isBlank(fileExtName)) {
            int idx = localFileName.lastIndexOf(".");
            if (idx > 0 && (localFileName.length() - idx <= Constants.FDFS_FILE_EXT_NAME_MAX_LEN + 1)) {
                fileExtName = localFileName.substring(idx + 1);
            }
        }
        boolean uploadSlave =
                (!Strings.isBlank(groupName)) && (!Strings.isBlank(masterFileName)) && (prefixName != null);
        byte[] fileExtNameByteArr = null;
        // new byte[Constants.FDFS_FILE_EXT_NAME_MAX_LEN];
        try {
            fileExtNameByteArr = localFileName.getBytes(fdfsClientConfig.getCharset());
            fileExtNameByteArr = Bytes.wrap(fileExtNameByteArr, Constants.FDFS_FILE_EXT_NAME_MAX_LEN);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Connection con = null;
        byte[] sizeBytes = null;
        long bodyLength = 0;
        byte[] fileBuf = null;
        FileInputStream fis = null;
        byte[] masterFileNameLengthByteArr = new byte[8];
        // 获取本地文件data
        try {
            fis = new FileInputStream(localFileName);
            // TODO 文件太大，这里可能会有问题。
            fileBuf = new byte[fis.available()];
            fis.read(fileBuf);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOs.close(fis);
        }

        if (uploadSlave) {
            con = getUpdateableConnection(groupName, masterFileName);
            byte[] prefixNameByteArr = null;
            byte[] masterFileNameByteArr = null;
            // new byte[Constants.FDFS_FILE_EXT_NAME_MAX_LEN];
            try {
                prefixNameByteArr = prefixName.getBytes(fdfsClientConfig.getCharset());
                masterFileNameByteArr = masterFileName.getBytes(fdfsClientConfig.getCharset());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            sizeBytes = new byte[2 * Constants.FDFS_PROTO_PKG_LEN_SIZE];
            bodyLength =
                    sizeBytes.length + Constants.FDFS_FILE_PREFIX_MAX_LEN
                            + Constants.FDFS_FILE_EXT_NAME_MAX_LEN + masterFileNameByteArr.length
                            + fileBuf.length;
            Bytes.long2bytes(masterFileName.length(), masterFileNameLengthByteArr);
        }
        else {
            con = getWriteableConnection(groupName);
        }

        return null;
    }


    @Override
    public String[] uploadFile(String groupName, String masterFileName, String prefixName, byte[] fileBuff,
            String fileExtName, Map<String, String> meta) {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public String[] uploadFile(String groupName, String masterFileName, String prefixName, byte[] fileBuff,
            int offset, int length, String fileExtName, Map<String, String> meta) {
        // TODO Auto-generated method stub
        return null;
    }



    @Override
    public String[] uploadAppenderFile(String localFileName, String fileExtName, Map<String, String> meta) {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public String[] uploadAppenderFile(byte[] fileBuff, int offset, int length, String fileExtName,
            Map<String, String> meta) {
        // TODO Auto-generated method stub
        return null;
    }




    @Override
    public String[] uploadAppenderFile(byte[] fileBuff, String fileExtName, Map<String, String> meta) {
        // TODO Auto-generated method stub
        return null;
    }




    @Override
    public int appendFile(String groupName, String appenderFileName, String localFileName) {
        // TODO Auto-generated method stub
        return 0;
    }


    @Override
    public int appendFile(String groupName, String appenderFileName, byte[] fileBuff) {
        // TODO Auto-generated method stub
        return 0;
    }




    @Override
    public int modifyFile(String groupName, String appenderFileName, long fileOffset, String localFileName) {
        // TODO Auto-generated method stub
        return 0;
    }


    @Override
    public int modifyFile(String groupName, String appenderFileName, long fileOffset, byte[] fileBuff) {
        // TODO Auto-generated method stub
        return 0;
    }


    @Override
    public int modifyFile(String groupName, String appenderFileName, long fileOffset, byte[] fileBuff,
            int bufferOffset, int bufferLength) {
        // TODO Auto-generated method stub
        return 0;
    }




    @Override
    public int deleteFile(String groupName, String remoteFileName) {
        // TODO Auto-generated method stub
        return 0;
    }


    @Override
    public int truncateFile(String groupName, String appenderFileName) {
        // TODO Auto-generated method stub
        return 0;
    }


    @Override
    public int truncateFile(String groupName, String appenderFileName, long truncatedFileSize) {
        // TODO Auto-generated method stub
        return 0;
    }


    @Override
    public byte[] downloadFile(String groupName, String remoteFileName) {
        long fileOffset = 0;
        long downloadBytes = 0;
        return downloadFile(groupName, remoteFileName, fileOffset, downloadBytes);
    }


    @Override
    public byte[] downloadFile(String groupName, String remoteFileName, long fileOffset, long downloadBytes) {
        Connection con = getFetchableConnection(groupName, remoteFileName);
        OutputStream os = null;
        InputStream is = null;
        try {
            os = con.getOutputStream();
            is = con.getInputStream();
            sendDownloadRequest(os, groupName, remoteFileName, fileOffset, downloadBytes);
            ResponseBody responseBody = Messages.readStorageResponse(is, -1);
            this.errorNo = responseBody.getErrorNo();
            if (this.errorNo != 0) {
                return null;
            }
            byte[] body = responseBody.getBody();
            return body;
        } catch (Exception e) {
            logger.error("downloadFile(String, String, long, long) fail");
            logger.error(e.getMessage(), e);
        } finally {
            closeSocket(con, is, os);
        }
        return null;
    }


    @Override
    public int downloadFile(String groupName, String remoteFileName, String localFileName) {
        byte[] data = downloadFile(groupName, remoteFileName);
        writeLocalFile(data, localFileName);
        return 0;
    }



    @Override
    public Map<String, String> getMetadata(String groupName, String remoteFileName) {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public int setMetadataResult(String groupName, String remoteFileName, Map<String, String> meta,
            byte opFlag) {
        // TODO Auto-generated method stub
        return 0;
    }


    @Override
    public FileInfo getFileInfo(String groupName, String remoteFileName) {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public FileInfo queryFileInfo(String groupName, String remoteFileName) {
        // TODO Auto-generated method stub
        return null;
    }


    void sendDownloadRequest(OutputStream os, String groupName, String remoteFileName, long fileOffset,
            long downloadBytes) throws Exception {
        byte[] groupNameByteArr = null;
        byte[] remoteFileNameByteArr = null;
        try {
            groupNameByteArr = groupName.getBytes(fdfsClientConfig.getCharset());
            remoteFileNameByteArr = remoteFileName.getBytes(fdfsClientConfig.getCharset());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte errorNo = 0;
        int headerLength = 10;
        int fileOffsetByteArrLength = 8;// long型是8个字节。
        int downloadBytesByteArrLength = 8;
        int groupNameByteArrLength = FDFS_GROUP_NAME_MAX_LEN;
        int remoteFileNameByteArrLength = remoteFileNameByteArr.length;
        int bodyLength =
                fileOffsetByteArrLength + downloadBytesByteArrLength + groupNameByteArrLength
                        + remoteFileNameByteArrLength;
        int requestTotalLength = headerLength + bodyLength;
        WriteByteArrayFragment request = new WriteByteArrayFragment(requestTotalLength);
        // fill header
        {
            request.writeLong(bodyLength);
            request.writeByte(STORAGE_PROTO_CMD_DOWNLOAD_FILE);
            request.writeByte(errorNo);
        }
        // fill body
        {
            request.writeLong(fileOffset);
            request.writeLong(downloadBytes);
            request.writeSubBytes(groupNameByteArr, FDFS_GROUP_NAME_MAX_LEN);
            request.writeBytes(remoteFileNameByteArr);
        }
        os.write(request.getData());
    }


    private void closeSocket(Connection con, InputStream is, OutputStream os) {
        try {
            if (is != null) {
                is.close();
            }
            if (os != null) {
                os.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void writeLocalFile(byte[] data, String localFileName) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(localFileName);
            fos.write(data);
        } catch (Exception e) {
            logger.error("downloadFile(String, String, String)");
            logger.error(e.getMessage(), e);
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
            }
        }
    }
}
