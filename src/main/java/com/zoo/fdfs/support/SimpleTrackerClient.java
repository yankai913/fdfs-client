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

import com.zoo.fdfs.api.FdfsClientConfig;
import com.zoo.fdfs.api.FdfsException;
import com.zoo.fdfs.api.GroupStat;
import com.zoo.fdfs.api.StorageConfig;
import com.zoo.fdfs.api.StorageStat;
import com.zoo.fdfs.api.TrackerClient;
import com.zoo.fdfs.api.TrackerGroup;
import com.zoo.fdfs.common.Messages;
import com.zoo.fdfs.common.ReadByteArrayFragment;
import com.zoo.fdfs.common.ResponseBody;
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

    private FdfsClientConfig fdfsClientConfig;

    private TrackerGroup trackerGroup;


    public SimpleTrackerClient(FdfsClientConfig fdfsClientConfig) {
        this.fdfsClientConfig = fdfsClientConfig;
        this.trackerGroup = new TrackerGroup(getFdfsClientConfig());
    }


    private FdfsClientConfig getFdfsClientConfig() {
        return fdfsClientConfig;
    }


    @Override
    public Set<String> getTrackerServerAddrSet() {
        return this.trackerGroup.getAvailableTrackerServerAddrSet();
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public StorageConfig getStoreStorageOne(String groupName) throws FdfsException {
        Socket socket = getSocket();
        InputStream is = null;
        OutputStream os = null;
        try {
            os = socket.getOutputStream();
            is = socket.getInputStream();
            byte cmd = 0, errorNo = 0;
            if (Strings.isBlank(groupName)) {
                cmd = TRACKER_PROTO_CMD_SERVICE_QUERY_STORE_WITHOUT_GROUP_ONE;
            }
            else {
                cmd = TRACKER_PROTO_CMD_SERVICE_QUERY_STORE_WITH_GROUP_ONE;
            }
            byte[] request = Messages.createRequest(groupName, fdfsClientConfig.getCharset(), cmd, errorNo);
            os.write(request);
            // recv
            ResponseBody responseBody = Messages.readResponse(is, TRACKER_QUERY_STORAGE_STORE_BODY_LEN);
            errorNo = responseBody.getErrorNo();
            if (errorNo != 0) {
                return null;
            }
            byte[] body = responseBody.getBody();
            ReadByteArrayFragment response = new ReadByteArrayFragment(body);
            String addr = response.readString(FDFS_GROUP_NAME_MAX_LEN, FDFS_IPADDR_SIZE - 1).trim();
            long port = response.readLong();
            byte storePathIndex = response.readByte();
            InetSocketAddress inetSocketAddress = new InetSocketAddress(addr, (int) port);
            StorageConfig storageConfig = new StorageConfig();
            storageConfig.setInetSocketAddress(inetSocketAddress);
            storageConfig.setStorePathIndex(storePathIndex);
            return storageConfig;
        } catch (Exception e) {
            String msg = "getStoreStorageOne fail, groupName=[{}], socket=[{}]";
            msg = Strings.format(msg, groupName, socket);
            throw new FdfsException(msg + e.getMessage(), e);
        } finally {
            closeSocket(socket, is, os);
        }
    }


    @Override
    public Set<StorageConfig> getStoreStorageSet(String groupName) throws FdfsException {
        Socket socket = getSocket();
        InputStream is = null;
        OutputStream os = null;
        try {
            os = socket.getOutputStream();
            is = socket.getInputStream();
            byte cmd = 0, errorNo = 0;
            if (Strings.isBlank(groupName)) {
                cmd = TRACKER_PROTO_CMD_SERVICE_QUERY_STORE_WITHOUT_GROUP_ALL;
            }
            else {
                cmd = TRACKER_PROTO_CMD_SERVICE_QUERY_STORE_WITH_GROUP_ALL;
            }
            byte[] request = Messages.createRequest(groupName, fdfsClientConfig.getCharset(), cmd, errorNo);
            os.write(request);
            // -1 jump over header check
            ResponseBody responseBody = Messages.readResponse(is, -1);
            errorNo = responseBody.getErrorNo();
            if (errorNo != 0) {
                return null;
            }
            byte[] body = responseBody.getBody();
            if (body.length < TRACKER_QUERY_STORAGE_STORE_BODY_LEN) {
                throw new IllegalStateException("invalid body.length, errorCode: " + ERR_NO_EINVAL);
            }
            int ipPortLength = body.length - (FDFS_GROUP_NAME_MAX_LEN + 1);
            int recordLength = FDFS_IPADDR_SIZE - 1 + FDFS_PROTO_PKG_LEN_SIZE;
            if (ipPortLength % recordLength != 0) {
                throw new IllegalStateException("invalid body.length, errorCode: " + ERR_NO_EINVAL);
            }
            int serverCount = ipPortLength / recordLength;
            if (serverCount > 16) {
                throw new IllegalStateException("invalid body.length, errorCode: " + ERR_NO_ENOSPC);
            }
            byte storePathIndex = body[body.length - 1];
            ReadByteArrayFragment response = new ReadByteArrayFragment(body);
            response.skip(FDFS_GROUP_NAME_MAX_LEN);
            Set<StorageConfig> set = new HashSet<StorageConfig>();
            for (int i = 0; i < serverCount; i++) {
                String addr = response.readString(0, FDFS_IPADDR_SIZE - 1).trim();
                long port = response.readLong();
                InetSocketAddress inetSocketAddress = new InetSocketAddress(addr, (int) port);
                StorageConfig storageConfig = new StorageConfig();
                storageConfig.setInetSocketAddress(inetSocketAddress);
                storageConfig.setStorePathIndex(storePathIndex);
                set.add(storageConfig);
            }
            return set;
        } catch (Exception e) {
            String msg = "getStoreStorageSet fail, groupName=[{}], socket=[{}]";
            msg = Strings.format(msg, groupName, socket);
            throw new FdfsException(msg + e.getMessage(), e);
        } finally {
            closeSocket(socket, is, os);
        }
    }


    public StorageConfig getFetchStorageOne(String groupName, String fileName) throws FdfsException {
        Set<StorageConfig> set = getFetchStorageSet(groupName, fileName);
        if (set != null && set.size() > 0) {
            Iterator<StorageConfig> ite = set.iterator();
            return ite.next();
        }
        return null;
    }


    private Set<StorageConfig> getFetchStorageSet(byte cmd, String groupName, String fileName)
            throws FdfsException {
        Socket socket = getSocket();
        InputStream is = null;
        OutputStream os = null;
        byte errorNo = 0;
        try {
            os = socket.getOutputStream();
            is = socket.getInputStream();
            byte[] request =
                    Messages.createRequest(groupName, fileName, fdfsClientConfig.getCharset(), cmd, errorNo);
            os.write(request);
            // // -1 jump over header check
            ResponseBody responseBody = Messages.readResponse(is, -1);
            errorNo = responseBody.getErrorNo();
            if (errorNo != 0) {
                return null;
            }
            byte[] body = responseBody.getBody();
            if (body.length < TRACKER_QUERY_STORAGE_FETCH_BODY_LEN) {
                throw new IllegalStateException("invalid body.length: " + body.length);
            }
            if ((body.length - TRACKER_QUERY_STORAGE_FETCH_BODY_LEN) % (FDFS_IPADDR_SIZE - 1) != 0) {
                throw new IllegalStateException("invalid body.length: " + body.length);
            }
            int serverCount =
                    1 + (body.length - TRACKER_QUERY_STORAGE_FETCH_BODY_LEN) / (FDFS_IPADDR_SIZE - 1);
            ReadByteArrayFragment response = new ReadByteArrayFragment(body);
            response.skip(FDFS_GROUP_NAME_MAX_LEN);
            Set<StorageConfig> set = new HashSet<StorageConfig>();
            for (int i = 0; i < serverCount; i++) {
                String addr = response.readString(0, FDFS_IPADDR_SIZE - 1).trim();
                long port = response.readLong();
                InetSocketAddress inetSocketAddress = new InetSocketAddress(addr, (int) port);
                StorageConfig storageConfig = new StorageConfig();
                storageConfig.setInetSocketAddress(inetSocketAddress);
                set.add(storageConfig);
            }
            return set;
        } catch (Exception e) {
            String msg = "getFetchStorageSet fail, groupName=[{}], fileName=[{}], socket=[{}]";
            msg = Strings.format(msg, groupName, socket);
            throw new FdfsException(msg + e.getMessage(), e);
        } finally {
            closeSocket(socket, is, os);
        }
    }


    @Override
    public GroupStat[] listGroups() throws FdfsException {
        Socket socket = getSocket();
        byte[] header = Messages.createHeader(0L, TRACKER_PROTO_CMD_SERVER_LIST_GROUP);
        InputStream is = null;
        OutputStream os = null;
        GroupStat[] groupStatArr = null;
        try {
            os = socket.getOutputStream();
            is = socket.getInputStream();
            os.write(header);
            ResponseBody responseBody = Messages.readResponse(is, -1);
            byte errorNo = responseBody.getErrorNo();
            if (errorNo != 0) {
                return null;
            }
            byte[] body = responseBody.getBody();
            int fieldTotal = GroupStat.getFieldsTotalSize();
            if (body.length % fieldTotal != 0) {
                throw new IllegalStateException("invalid body.length");
            }
            groupStatArr =
                    Messages.decode(body, GroupStat.class, GroupStat.getFieldsTotalSize(),
                        fdfsClientConfig.getCharset());
            return groupStatArr;
        } catch (Exception e) {
            String msg = "listGroups fail, groupName=[{}], fileName=[{}] socket=[{}]";
            msg = Strings.format(msg, socket);
            throw new FdfsException(msg + e.getMessage(), e);
        } finally {
            closeSocket(socket, is, os);
        }
    }


    private StorageStat[] listStorages(Socket socket, String groupName, String storageServerAddr)
            throws FdfsException {
        if (Strings.isBlank(groupName)) {
            throw new IllegalArgumentException("groupName is blank");
        }
        InputStream is = null;
        OutputStream os = null;
        byte cmd = TRACKER_PROTO_CMD_SERVER_LIST_STORAGE;

        byte[] storageServerAddrByteArr = null;
        int storageServerAddrByteArrLen = 0;
        try {
            os = socket.getOutputStream();
            is = socket.getInputStream();
            byte[] groupNameByteArr = groupName.getBytes(fdfsClientConfig.getCharset());
            if (Strings.isNotBlank(storageServerAddr)) {
                storageServerAddrByteArr = storageServerAddr.getBytes(fdfsClientConfig.getCharset());
                if (storageServerAddrByteArr.length < FDFS_IPADDR_SIZE) {
                    storageServerAddrByteArrLen = storageServerAddrByteArr.length;
                }
                else {
                    storageServerAddrByteArrLen = FDFS_IPADDR_SIZE - 1;
                }
            }
            int bodyLength = FDFS_GROUP_NAME_MAX_LEN + storageServerAddrByteArrLen;
            int totalLength = 10 + bodyLength;
            WriteByteArrayFragment request = new WriteByteArrayFragment(totalLength);
            {
                request.writeLong(bodyLength);
                request.writeByte(cmd);
                request.writeByte((byte) 0);
            }
            {
                request.writeLimitedBytes(groupNameByteArr, FDFS_GROUP_NAME_MAX_LEN);
                if (storageServerAddrByteArr != null) {
                    request.writeLimitedBytes(storageServerAddrByteArr, storageServerAddrByteArrLen);
                }
            }
            os.write(request.getData());
            ResponseBody responseBody = Messages.readResponse(is, -1);
            byte errorNo = responseBody.getErrorNo();
            if (errorNo != 0) {
                return null;
            }
            byte[] body = responseBody.getBody();
            int fieldTotal = StorageStat.getFieldsTotalSize();
            if (body.length % fieldTotal != 0) {
                throw new IllegalStateException("invalid body.length");
            }
            StorageStat[] storageStatArr =
                    Messages.decode(body, StorageStat.class, StorageStat.getFieldsTotalSize(),
                        fdfsClientConfig.getCharset());
            return storageStatArr;
        } catch (Exception e) {
            String msg = "listStorages fail, groupName=[{}], storageServerAddr=[{}] socket=[{}]";
            msg = Strings.format(msg, groupName, storageServerAddr, socket);
            throw new FdfsException(msg + e.getMessage(), e);
        } finally {
            closeSocket(socket, is, os);
        }
    }


    @Override
    public StorageStat[] listStorages(String groupName, String storageServerAddr) throws FdfsException {
        if (Strings.isBlank(groupName)) {
            throw new IllegalArgumentException("groupName is blank");
        }
        Socket socket = getSocket();
        return listStorages(socket, groupName, storageServerAddr);
    }


    @Override
    public StorageConfig getUpdateStorage(String groupName, String fileName) throws FdfsException {
        byte cmd = TRACKER_PROTO_CMD_SERVICE_QUERY_UPDATE;
        Set<StorageConfig> set = getFetchStorageSet(cmd, groupName, fileName);
        if (set != null && set.size() > 0) {
            Iterator<StorageConfig> ite = set.iterator();
            return ite.next();
        }
        return null;
    }


    private void checkGroupNameNotBlank(String groupName) {
        if (Strings.isBlank(groupName)) {
            throw new IllegalArgumentException("groupName is blank");
        }
    }


    private void checkAddrNotBlank(String storageServerAddr) {
        if (Strings.isBlank(storageServerAddr)) {
            throw new IllegalArgumentException("storageServerAddr is blank");
        }
    }


    @Override
    public boolean deleteStorage(TrackerGroup trackerGroup, String groupName, String storageServerAddr)
            throws FdfsException {
        checkAddrNotBlank(storageServerAddr);
        checkGroupNameNotBlank(groupName);
        Set<Socket> set = trackerGroup.getGroupSocket();
        if (set == null || set.size() == 0) {
            logger.error("deleteStorage fail, set is blank");
            return false;
        }
        int notFoundCount = 0;
        byte errorNo = 0;
        for (Socket socket : set) {
            StorageStat[] storageStatArr = listStorages(socket, groupName, storageServerAddr);
            if (storageStatArr == null) {
                if (errorNo == ERR_NO_ENOENT) {
                    notFoundCount++;
                }
                else {
                    return false;
                }
            }
            else if (storageStatArr.length == 0) {
                notFoundCount++;
            }
            else if (storageStatArr[0].getStatus() == FDFS_STORAGE_STATUS_ONLINE
                    || storageStatArr[0].getStatus() == FDFS_STORAGE_STATUS_ACTIVE) {
                errorNo = ERR_NO_EBUSY;
                return false;
            }
            else {
                logger.warn("unknow...");
            }
        }
        if (notFoundCount == set.size()) {
            errorNo = ERR_NO_ENOENT;
            return false;
        }
        notFoundCount = 0;
        for (Socket socket : set) {
            try {
                if (!this.deleteStorage(socket, groupName, storageServerAddr)) {
                    if (errorNo != 0) {
                        if (errorNo == ERR_NO_ENOENT) {
                            notFoundCount++;
                        }
                        else if (errorNo != ERR_NO_EALREADY) {
                            return false;
                        }
                    }
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
        return false;
    }


    @Override
    public boolean deleteStorage(String groupName, String storageServerAddr) throws FdfsException {
        Socket socket = getSocket();
        return deleteStorage(socket, groupName, storageServerAddr);
    }


    private boolean deleteStorage(Socket socket, String groupName, String storageServerAddr)
            throws FdfsException {
        if (Strings.isBlank(groupName)) {
            throw new IllegalArgumentException("groupName is blank");
        }
        if (Strings.isBlank(storageServerAddr)) {
            throw new IllegalArgumentException("storageServerAddr is blank");
        }
        InputStream is = null;
        OutputStream os = null;
        byte cmd = TRACKER_PROTO_CMD_SERVER_DELETE_STORAGE;
        try {
            os = socket.getOutputStream();
            is = socket.getInputStream();
            byte[] groupNameByteArr = groupName.getBytes(fdfsClientConfig.getCharset());
            byte[] storageServerAddrByteArr = storageServerAddr.getBytes(fdfsClientConfig.getCharset());
            int storageServerAddrByteArrLen = 0;
            if (storageServerAddrByteArr.length < FDFS_IPADDR_SIZE) {
                storageServerAddrByteArrLen = storageServerAddrByteArr.length;
            }
            else {
                storageServerAddrByteArrLen = FDFS_IPADDR_SIZE - 1;
            }
            int bodyLength = FDFS_GROUP_NAME_MAX_LEN + storageServerAddrByteArrLen;
            int totalLength = 10 + bodyLength;
            WriteByteArrayFragment request = new WriteByteArrayFragment(totalLength);
            {
                request.writeLong(bodyLength);
                request.writeByte(cmd);
                request.writeByte((byte) 0);
            }
            {
                request.writeLimitedBytes(groupNameByteArr, FDFS_GROUP_NAME_MAX_LEN);
                request.writeLimitedBytes(storageServerAddrByteArr, storageServerAddrByteArrLen);
            }
            os.write(request.getData());
            ResponseBody responseBody = Messages.readResponse(is, -1);
            byte errorNo = responseBody.getErrorNo();
            if (errorNo != 0) {
                return false;
            }
            byte[] body = responseBody.getBody();
            assert body.length > 0;
            return true;
        } catch (Exception e) {
            String msg = "deleteStorage fail, groupName=[{}], storageServerAddr=[{}] socket=[{}]";
            msg = Strings.format(msg, groupName, storageServerAddr, socket);
            throw new FdfsException(msg + e.getMessage(), e);
        } finally {
            closeSocket(socket, is, os);
        }
    }


    @Override
    public Set<StorageConfig> getFetchStorageSet(String groupName, String fileName) throws FdfsException {
        byte cmd = TRACKER_PROTO_CMD_SERVICE_QUERY_UPDATE;
        Set<StorageConfig> set = getFetchStorageSet(cmd, groupName, fileName);
        if (set != null && set.size() > 0) {
            return set;
        }
        return null;
    }
}
