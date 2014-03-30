package com.zoo.fdfs.support;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zoo.fdfs.api.Constants;
import com.zoo.fdfs.api.FdfsClientConfigurable;
import com.zoo.fdfs.api.GroupStat;
import com.zoo.fdfs.api.StorageConfig;
import com.zoo.fdfs.api.StorageStat;
import com.zoo.fdfs.api.TrackerClient;
import com.zoo.fdfs.api.TrackerGroup;
import com.zoo.fdfs.common.Messages;
import com.zoo.fdfs.common.ReadByteArrayFragment;
import com.zoo.fdfs.common.Strings;
import com.zoo.fdfs.common.WriteByteArrayFragment;


/**
 * Prototype, Threadsafe
 * 
 * @author yankai913@gmail.com
 * @date 2014-3-14
 */
public class SimpleTrackerClient implements TrackerClient {

    private static final Logger logger = LoggerFactory.getLogger(SimpleTrackerClient.class);

    // private final Set<StorageConfig> storageServerAddrSet = new
    // HashSet<StorageConfig>();

    private FdfsClientConfigurable fdfsClientConfigurable;

    private TrackerGroup trackerGroup;


    public SimpleTrackerClient(FdfsClientConfigurable fdfsClientConfigurable) {
        this.fdfsClientConfigurable = fdfsClientConfigurable;
        this.trackerGroup = new TrackerGroup(this.fdfsClientConfigurable);
    }


    @Override
    public Set<String> getTrackerServerAddrSet() {
        return this.trackerGroup.getAvailableTrackerServerAddrSet();
    }


    private void sendGetStoreStorageRequest(OutputStream os, byte cmd, String groupName) throws Exception {
        byte[] groupNameByteArr = null;
        byte errorNo = 0;
        int bodyLength = 0;
        if (Strings.isBlank(groupName)) {
            bodyLength = 0;
        }
        else {
            bodyLength = Constants.FDFS_GROUP_NAME_MAX_LEN;
            groupNameByteArr = groupName.getBytes(fdfsClientConfigurable.getCharset());
        }
        int headerLenght = 10;
        int totalLength = headerLenght + bodyLength;
        WriteByteArrayFragment request = new WriteByteArrayFragment(totalLength);
        // writeHeader
        {
            request.writeLong(bodyLength);
            request.writeByte(cmd);
            request.writeByte(errorNo);
        }
        // writeBody
        {
            if (!Strings.isBlank(groupName)) {
                request.writeSubBytes(groupNameByteArr, Constants.FDFS_GROUP_NAME_MAX_LEN);
            }
        }
        // write
        os.write(request.getData());
    }


    private Socket getSocket() {
        Socket socket = trackerGroup.getAvailableSocket();
        if (socket == null) {
            throw new IllegalStateException("socket is null");
        }
        return socket;
    }


    private void closeSocket(Socket socket, InputStream is, OutputStream os) {
        try {
            if (is != null) {
                is.close();
            }
            if (os != null) {
                os.close();
            }
            if (socket != null) {
                socket.close();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public StorageConfig getStoreStorageOne(String groupName) {
        Socket socket = getSocket();
        InputStream is = null;
        OutputStream os = null;
        try {
            os = socket.getOutputStream();
            is = socket.getInputStream();
            byte cmd = 0;
            if (Strings.isBlank(groupName)) {
                cmd = Constants.TRACKER_PROTO_CMD_SERVICE_QUERY_STORE_WITHOUT_GROUP_ONE;
            }
            else {
                cmd = Constants.TRACKER_PROTO_CMD_SERVICE_QUERY_STORE_WITH_GROUP_ONE;
            }
            sendGetStoreStorageRequest(os, cmd, groupName);
            byte[] responseBody = Messages.readResponse(is);
            ReadByteArrayFragment readByteArrayFragment = new ReadByteArrayFragment(responseBody);
            String addr =
                    readByteArrayFragment.readString(Constants.FDFS_GROUP_NAME_MAX_LEN,
                        Constants.FDFS_IPADDR_SIZE - 1).trim();
            long port = readByteArrayFragment.readLong();
            byte storePathIndex = readByteArrayFragment.readByte();
            InetSocketAddress inetSocketAddress = new InetSocketAddress(addr, (int) port);
            StorageConfig storageConfig = new StorageConfig();
            storageConfig.setInetSocketAddress(inetSocketAddress);
            storageConfig.setStorePathIndex(storePathIndex);
            return storageConfig;
        }
        catch (Exception e) {
            logger.error("getStoreStorageOne fail, socket: " + socket);
            logger.error(e.getMessage(), e);
        }
        finally {
            closeSocket(socket, is, os);
        }
        return null;
    }


    @Override
    public Set<StorageConfig> getStoreStorageSet(String groupName) {
        Socket socket = getSocket();
        Set<StorageConfig> set = new HashSet<StorageConfig>();
        InputStream is = null;
        OutputStream os = null;
        try {
            os = socket.getOutputStream();
            is = socket.getInputStream();
            byte cmd = 0;
            if (Strings.isBlank(groupName)) {
                cmd = Constants.TRACKER_PROTO_CMD_SERVICE_QUERY_STORE_WITHOUT_GROUP_ALL;
            }
            else {
                cmd = Constants.TRACKER_PROTO_CMD_SERVICE_QUERY_STORE_WITH_GROUP_ALL;
            }
            sendGetStoreStorageRequest(os, cmd, groupName);
            byte[] responseBody = Messages.readResponse(is);
            if (responseBody.length < Constants.TRACKER_QUERY_STORAGE_STORE_BODY_LEN) {
                throw new IllegalStateException("invalid responseBody.length, errorCode: "
                        + Constants.ERR_NO_EINVAL);
            }
            int ipPortLength = responseBody.length - (Constants.FDFS_GROUP_NAME_MAX_LEN + 1);
            int recordLength = Constants.FDFS_IPADDR_SIZE - 1 + Constants.FDFS_PROTO_PKG_LEN_SIZE;
            if (ipPortLength % recordLength != 0) {
                throw new IllegalStateException("invalid responseBody.length, errorCode: "
                        + Constants.ERR_NO_EINVAL);
            }
            int serverCount = ipPortLength / recordLength;
            if (serverCount > 16) {
                throw new IllegalStateException("invalid responseBody.length, errorCode: "
                        + Constants.ERR_NO_ENOSPC);
            }
            byte storePathIndex = responseBody[responseBody.length - 1];
            ReadByteArrayFragment readByteArrayFragment = new ReadByteArrayFragment(responseBody);
            readByteArrayFragment.skip(Constants.FDFS_GROUP_NAME_MAX_LEN);
            for (int i = 0; i < serverCount; i++) {
                String addr = readByteArrayFragment.readString(0, Constants.FDFS_IPADDR_SIZE - 1).trim();
                long port = readByteArrayFragment.readLong();
                InetSocketAddress inetSocketAddress = new InetSocketAddress(addr, (int) port);
                StorageConfig storageConfig = new StorageConfig();
                storageConfig.setInetSocketAddress(inetSocketAddress);
                storageConfig.setStorePathIndex(storePathIndex);
                set.add(storageConfig);
            }
        }
        catch (Exception e) {
            logger.error("getStoreStorageOne fail, socket: " + socket);
            logger.error(e.getMessage(), e);
        }
        finally {
            closeSocket(socket, is, os);
        }
        return set;
    }


    private void sendGetFetchStorageRequest(OutputStream os, byte cmd, String groupName, String fileName)
            throws Exception {
        byte[] groupNameByteArr = groupName.getBytes(fdfsClientConfigurable.getCharset());
        byte[] fileNameByteArr = fileName.getBytes(fdfsClientConfigurable.getCharset());
        byte errorNo = 0;
        int bodyLength = Constants.FDFS_GROUP_NAME_MAX_LEN + fileNameByteArr.length;
        int totalLength = 10 + bodyLength;
        WriteByteArrayFragment request = new WriteByteArrayFragment(totalLength);
        // writeHeader
        {
            request.writeLong(bodyLength);
            request.writeByte(cmd);
            request.writeByte(errorNo);
        }
        // writeBody
        {
            request.writeBytes(groupNameByteArr);
            request.writeBytes(fileNameByteArr);
        }
        // write
        os.write(request.getData());

    }


    public StorageConfig getFetchStorageOne(String groupName, String fileName) {
        Set<StorageConfig> set = getFetchStorageSet(groupName, fileName);
        if (set != null && set.size() > 0) {
            Iterator<StorageConfig> ite = set.iterator();
            return ite.next();
        }
        return null;
    }


    @Override
    public Set<StorageConfig> getFetchStorageSet(String groupName, String fileName) {
        Socket socket = getSocket();
        Set<StorageConfig> set = new HashSet<StorageConfig>();
        InputStream is = null;
        OutputStream os = null;
        try {
            os = socket.getOutputStream();
            is = socket.getInputStream();
            byte cmd = Constants.TRACKER_PROTO_CMD_SERVICE_QUERY_FETCH_ONE;
            sendGetFetchStorageRequest(os, cmd, groupName, fileName);
            byte[] responseBody = Messages.readResponse(is);
            if (responseBody.length < Constants.TRACKER_QUERY_STORAGE_FETCH_BODY_LEN) {
                throw new IllegalStateException("invalid responseBody.length: " + responseBody.length);
            }
            if ((responseBody.length - Constants.TRACKER_QUERY_STORAGE_FETCH_BODY_LEN)
                    % (Constants.FDFS_IPADDR_SIZE - 1) != 0) {
                throw new IllegalStateException("invalid responseBody.length: " + responseBody.length);
            }
            int serverCount =
                    1 + (responseBody.length - Constants.TRACKER_QUERY_STORAGE_FETCH_BODY_LEN)
                            / (Constants.FDFS_IPADDR_SIZE - 1);
            ReadByteArrayFragment readByteArrayFragment = new ReadByteArrayFragment(responseBody);
            readByteArrayFragment.skip(Constants.FDFS_GROUP_NAME_MAX_LEN);
            for (int i = 0; i < serverCount; i++) {
                String addr = readByteArrayFragment.readString(0, Constants.FDFS_IPADDR_SIZE - 1).trim();
                long port = readByteArrayFragment.readLong();
                InetSocketAddress inetSocketAddress = new InetSocketAddress(addr, (int) port);
                StorageConfig storageConfig = new StorageConfig();
                storageConfig.setInetSocketAddress(inetSocketAddress);
                set.add(storageConfig);
            }
        }
        catch (Exception e) {
            logger.error("getFetchStorageSet fail");
            logger.error(e.getMessage(), e);
        }
        finally {
            closeSocket(socket, is, os);
        }
        return set;

    }


    @Override
    public GroupStat[] listGroups() {
        Socket socket = getSocket();
        byte[] header = Messages.createHeader(0L, Constants.TRACKER_PROTO_CMD_SERVER_LIST_GROUP, (byte) 0);
        InputStream is = null;
        OutputStream os = null;
        GroupStat[] groupStatArr = null;
        try {
            os = socket.getOutputStream();
            is = socket.getInputStream();
            os.write(header);
            byte[] responseBody = Messages.readResponse(is);
            int fieldTotal = GroupStat.getFieldsTotalSize();
            if (responseBody.length % fieldTotal != 0) {
                throw new IllegalStateException("invalid responseBody.length");
            }
            groupStatArr = Messages.decode(responseBody, GroupStat.class, GroupStat.getFieldsTotalSize());
        }
        catch (Exception e) {
            logger.error("listGroups fail");
            logger.error(e.getMessage(), e);
        }
        finally {
            closeSocket(socket, is, os);
        }
        return groupStatArr;
    }


    @Override
    public StorageStat[] listStorages(String groupName, String storageServerAddr) {
        if (Strings.isBlank(groupName)) {
            throw new IllegalArgumentException("groupName is blank");
        }
        Socket socket = getSocket();
        InputStream is = null;
        OutputStream os = null;
        StorageStat[] storageStatArr = null;
        byte cmd = Constants.TRACKER_PROTO_CMD_SERVER_LIST_STORAGE;
        byte errorNo = 0;
        byte[] storageServerAddrByteArr = null;
        int storageServerAddrByteArrLen = 0;
        try {
            os = socket.getOutputStream();
            is = socket.getInputStream();
            byte[] groupNameByteArr = groupName.getBytes(fdfsClientConfigurable.getCharset());
            if (!Strings.isBlank(storageServerAddr)) {
                storageServerAddrByteArr = storageServerAddr.getBytes(fdfsClientConfigurable.getCharset());
                if (storageServerAddrByteArr.length < Constants.FDFS_IPADDR_SIZE) {
                    storageServerAddrByteArrLen = storageServerAddrByteArr.length;
                }
                else {
                    storageServerAddrByteArrLen = Constants.FDFS_IPADDR_SIZE - 1;
                }
            }
            int bodyLength = Constants.FDFS_GROUP_NAME_MAX_LEN + storageServerAddrByteArrLen;
            int totalLength = 10 + bodyLength;
            WriteByteArrayFragment request = new WriteByteArrayFragment(totalLength);
            {
                request.writeLong(bodyLength);
                request.writeByte(cmd);
                request.writeByte(errorNo);
            }
            {
                request.writeSubBytes(groupNameByteArr, Constants.FDFS_GROUP_NAME_MAX_LEN);
                if (storageServerAddrByteArr != null) {
                    request.writeSubBytes(storageServerAddrByteArr, storageServerAddrByteArrLen);
                }
            }
            os.write(request.getData());
            byte[] responseBody = Messages.readResponse(is);
            int fieldTotal = StorageStat.getFieldsTotalSize();
            if (responseBody.length % fieldTotal != 0) {
                throw new IllegalStateException("invalid responseBody.length");
            }
            storageStatArr =
                    Messages.decode(responseBody, StorageStat.class, StorageStat.getFieldsTotalSize());
        }
        catch (Exception e) {
            logger.error("listStorages fail");
            logger.error(e.getMessage(), e);
        }
        finally {
            closeSocket(socket, is, os);
        }
        return storageStatArr;
    }


    @Override
    public StorageConfig getUpdateStorage(String groupName, String fileName) {
        return null;
    }


    @Override
    public boolean deleteStorage(TrackerGroup trackerGroup, String groupName, String storageServerAddr) {
        return false;
    }
}
