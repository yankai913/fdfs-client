package com.zoo.fdfs.api;

import java.util.Date;
import static com.zoo.fdfs.api.Contants.FDFS_PROTO_PKG_LEN_SIZE;
import static com.zoo.fdfs.api.Contants.FDFS_STORAGE_ID_MAX_SIZE;
import static com.zoo.fdfs.api.Contants.FDFS_IPADDR_SIZE;
import static com.zoo.fdfs.api.Contants.FDFS_VERSION_SIZE;
import static com.zoo.fdfs.api.Contants.FDFS_DOMAIN_NAME_MAX_SIZE;


/**
 * 
 * @author yankai913@gmail.com
 * @date 2014-3-17
 */
public class StorageStat extends BaseStat {

    protected static final int FIELD_INDEX_STATUS = 0;
    protected static final int FIELD_INDEX_ID = 1;
    protected static final int FIELD_INDEX_IP_ADDR = 2;
    protected static final int FIELD_INDEX_DOMAIN_NAME = 3;
    protected static final int FIELD_INDEX_SRC_IP_ADDR = 4;
    protected static final int FIELD_INDEX_VERSION = 5;
    protected static final int FIELD_INDEX_JOIN_TIME = 6;
    protected static final int FIELD_INDEX_UP_TIME = 7;
    protected static final int FIELD_INDEX_TOTAL_MB = 8;
    protected static final int FIELD_INDEX_FREE_MB = 9;
    protected static final int FIELD_INDEX_UPLOAD_PRIORITY = 10;
    protected static final int FIELD_INDEX_STORE_PATH_COUNT = 11;
    protected static final int FIELD_INDEX_SUBDIR_COUNT_PER_PATH = 12;
    protected static final int FIELD_INDEX_CURRENT_WRITE_PATH = 13;
    protected static final int FIELD_INDEX_STORAGE_PORT = 14;
    protected static final int FIELD_INDEX_STORAGE_HTTP_PORT = 15;
    protected static final int FIELD_INDEX_TOTAL_UPLOAD_COUNT = 16;
    protected static final int FIELD_INDEX_SUCCESS_UPLOAD_COUNT = 17;
    protected static final int FIELD_INDEX_TOTAL_APPEND_COUNT = 18;
    protected static final int FIELD_INDEX_SUCCESS_APPEND_COUNT = 19;
    protected static final int FIELD_INDEX_TOTAL_MODIFY_COUNT = 20;
    protected static final int FIELD_INDEX_SUCCESS_MODIFY_COUNT = 21;
    protected static final int FIELD_INDEX_TOTAL_TRUNCATE_COUNT = 22;
    protected static final int FIELD_INDEX_SUCCESS_TRUNCATE_COUNT = 23;
    protected static final int FIELD_INDEX_TOTAL_SET_META_COUNT = 24;
    protected static final int FIELD_INDEX_SUCCESS_SET_META_COUNT = 25;
    protected static final int FIELD_INDEX_TOTAL_DELETE_COUNT = 26;
    protected static final int FIELD_INDEX_SUCCESS_DELETE_COUNT = 27;
    protected static final int FIELD_INDEX_TOTAL_DOWNLOAD_COUNT = 28;
    protected static final int FIELD_INDEX_SUCCESS_DOWNLOAD_COUNT = 29;
    protected static final int FIELD_INDEX_TOTAL_GET_META_COUNT = 30;
    protected static final int FIELD_INDEX_SUCCESS_GET_META_COUNT = 31;
    protected static final int FIELD_INDEX_TOTAL_CREATE_LINK_COUNT = 32;
    protected static final int FIELD_INDEX_SUCCESS_CREATE_LINK_COUNT = 33;
    protected static final int FIELD_INDEX_TOTAL_DELETE_LINK_COUNT = 34;
    protected static final int FIELD_INDEX_SUCCESS_DELETE_LINK_COUNT = 35;
    protected static final int FIELD_INDEX_TOTAL_UPLOAD_BYTES = 36;
    protected static final int FIELD_INDEX_SUCCESS_UPLOAD_BYTES = 37;
    protected static final int FIELD_INDEX_TOTAL_APPEND_BYTES = 38;
    protected static final int FIELD_INDEX_SUCCESS_APPEND_BYTES = 39;
    protected static final int FIELD_INDEX_TOTAL_MODIFY_BYTES = 40;
    protected static final int FIELD_INDEX_SUCCESS_MODIFY_BYTES = 41;
    protected static final int FIELD_INDEX_TOTAL_DOWNLOAD_BYTES = 42;
    protected static final int FIELD_INDEX_SUCCESS_DOWNLOAD_BYTES = 43;
    protected static final int FIELD_INDEX_TOTAL_SYNC_IN_BYTES = 44;
    protected static final int FIELD_INDEX_SUCCESS_SYNC_IN_BYTES = 45;
    protected static final int FIELD_INDEX_TOTAL_SYNC_OUT_BYTES = 46;
    protected static final int FIELD_INDEX_SUCCESS_SYNC_OUT_BYTES = 47;
    protected static final int FIELD_INDEX_TOTAL_FILE_OPEN_COUNT = 48;
    protected static final int FIELD_INDEX_SUCCESS_FILE_OPEN_COUNT = 49;
    protected static final int FIELD_INDEX_TOTAL_FILE_READ_COUNT = 50;
    protected static final int FIELD_INDEX_SUCCESS_FILE_READ_COUNT = 51;
    protected static final int FIELD_INDEX_TOTAL_FILE_WRITE_COUNT = 52;
    protected static final int FIELD_INDEX_SUCCESS_FILE_WRITE_COUNT = 53;
    protected static final int FIELD_INDEX_LAST_SOURCE_UPDATE = 54;
    protected static final int FIELD_INDEX_LAST_SYNC_UPDATE = 55;
    protected static final int FIELD_INDEX_LAST_SYNCED_TIMESTAMP = 56;
    protected static final int FIELD_INDEX_LAST_HEART_BEAT_TIME = 57;
    protected static final int FIELD_INDEX_IF_TRUNK_FILE = 58;

    protected static int fieldsTotalSize;
    protected static FieldMeta[] fieldMetaArr = new FieldMeta[59];

    static {
        int offset = 0;

        fieldMetaArr[FIELD_INDEX_STATUS] = new FieldMeta("status", offset, 1);
        offset += 1;

        fieldMetaArr[FIELD_INDEX_ID] = new FieldMeta("id", offset, FDFS_STORAGE_ID_MAX_SIZE);
        offset += FDFS_STORAGE_ID_MAX_SIZE;

        fieldMetaArr[FIELD_INDEX_IP_ADDR] = new FieldMeta("ipAddr", offset, FDFS_IPADDR_SIZE);
        offset += FDFS_IPADDR_SIZE;

        fieldMetaArr[FIELD_INDEX_DOMAIN_NAME] =
                new FieldMeta("domainName", offset, FDFS_DOMAIN_NAME_MAX_SIZE);
        offset += FDFS_DOMAIN_NAME_MAX_SIZE;

        fieldMetaArr[FIELD_INDEX_SRC_IP_ADDR] = new FieldMeta("srcIpAddr", offset, FDFS_IPADDR_SIZE);
        offset += FDFS_IPADDR_SIZE;

        fieldMetaArr[FIELD_INDEX_VERSION] = new FieldMeta("version", offset, FDFS_VERSION_SIZE);
        offset += FDFS_VERSION_SIZE;

        fieldMetaArr[FIELD_INDEX_JOIN_TIME] = new FieldMeta("joinTime", offset, FDFS_PROTO_PKG_LEN_SIZE);
        offset += FDFS_PROTO_PKG_LEN_SIZE;

        fieldMetaArr[FIELD_INDEX_UP_TIME] = new FieldMeta("upTime", offset, FDFS_PROTO_PKG_LEN_SIZE);
        offset += FDFS_PROTO_PKG_LEN_SIZE;

        fieldMetaArr[FIELD_INDEX_TOTAL_MB] = new FieldMeta("totalMB", offset, FDFS_PROTO_PKG_LEN_SIZE);
        offset += FDFS_PROTO_PKG_LEN_SIZE;

        fieldMetaArr[FIELD_INDEX_FREE_MB] = new FieldMeta("freeMB", offset, FDFS_PROTO_PKG_LEN_SIZE);
        offset += FDFS_PROTO_PKG_LEN_SIZE;

        fieldMetaArr[FIELD_INDEX_UPLOAD_PRIORITY] =
                new FieldMeta("uploadPriority", offset, FDFS_PROTO_PKG_LEN_SIZE);
        offset += FDFS_PROTO_PKG_LEN_SIZE;

        fieldMetaArr[FIELD_INDEX_STORE_PATH_COUNT] =
                new FieldMeta("storePathCount", offset, FDFS_PROTO_PKG_LEN_SIZE);
        offset += FDFS_PROTO_PKG_LEN_SIZE;

        fieldMetaArr[FIELD_INDEX_SUBDIR_COUNT_PER_PATH] =
                new FieldMeta("subdirCountPerPath", offset, FDFS_PROTO_PKG_LEN_SIZE);
        offset += FDFS_PROTO_PKG_LEN_SIZE;

        fieldMetaArr[FIELD_INDEX_CURRENT_WRITE_PATH] =
                new FieldMeta("currentWritePath", offset, FDFS_PROTO_PKG_LEN_SIZE);
        offset += FDFS_PROTO_PKG_LEN_SIZE;

        fieldMetaArr[FIELD_INDEX_STORAGE_PORT] =
                new FieldMeta("storagePort", offset, FDFS_PROTO_PKG_LEN_SIZE);
        offset += FDFS_PROTO_PKG_LEN_SIZE;

        fieldMetaArr[FIELD_INDEX_STORAGE_HTTP_PORT] =
                new FieldMeta("storageHttpPort", offset, FDFS_PROTO_PKG_LEN_SIZE);
        offset += FDFS_PROTO_PKG_LEN_SIZE;

        fieldMetaArr[FIELD_INDEX_TOTAL_UPLOAD_COUNT] =
                new FieldMeta("totalUploadCount", offset, FDFS_PROTO_PKG_LEN_SIZE);
        offset += FDFS_PROTO_PKG_LEN_SIZE;

        fieldMetaArr[FIELD_INDEX_SUCCESS_UPLOAD_COUNT] =
                new FieldMeta("successUploadCount", offset, FDFS_PROTO_PKG_LEN_SIZE);
        offset += FDFS_PROTO_PKG_LEN_SIZE;

        fieldMetaArr[FIELD_INDEX_TOTAL_APPEND_COUNT] =
                new FieldMeta("totalAppendCount", offset, FDFS_PROTO_PKG_LEN_SIZE);
        offset += FDFS_PROTO_PKG_LEN_SIZE;

        fieldMetaArr[FIELD_INDEX_SUCCESS_APPEND_COUNT] =
                new FieldMeta("successAppendCount", offset, FDFS_PROTO_PKG_LEN_SIZE);
        offset += FDFS_PROTO_PKG_LEN_SIZE;

        fieldMetaArr[FIELD_INDEX_TOTAL_MODIFY_COUNT] =
                new FieldMeta("totalModifyCount", offset, FDFS_PROTO_PKG_LEN_SIZE);
        offset += FDFS_PROTO_PKG_LEN_SIZE;

        fieldMetaArr[FIELD_INDEX_SUCCESS_MODIFY_COUNT] =
                new FieldMeta("successModifyCount", offset, FDFS_PROTO_PKG_LEN_SIZE);
        offset += FDFS_PROTO_PKG_LEN_SIZE;

        fieldMetaArr[FIELD_INDEX_TOTAL_TRUNCATE_COUNT] =
                new FieldMeta("totalTruncateCount", offset, FDFS_PROTO_PKG_LEN_SIZE);
        offset += FDFS_PROTO_PKG_LEN_SIZE;

        fieldMetaArr[FIELD_INDEX_SUCCESS_TRUNCATE_COUNT] =
                new FieldMeta("successTruncateCount", offset, FDFS_PROTO_PKG_LEN_SIZE);
        offset += FDFS_PROTO_PKG_LEN_SIZE;

        fieldMetaArr[FIELD_INDEX_TOTAL_SET_META_COUNT] =
                new FieldMeta("totalSetMetaCount", offset, FDFS_PROTO_PKG_LEN_SIZE);
        offset += FDFS_PROTO_PKG_LEN_SIZE;

        fieldMetaArr[FIELD_INDEX_SUCCESS_SET_META_COUNT] =
                new FieldMeta("successSetMetaCount", offset, FDFS_PROTO_PKG_LEN_SIZE);
        offset += FDFS_PROTO_PKG_LEN_SIZE;

        fieldMetaArr[FIELD_INDEX_TOTAL_DELETE_COUNT] =
                new FieldMeta("totalDeleteCount", offset, FDFS_PROTO_PKG_LEN_SIZE);
        offset += FDFS_PROTO_PKG_LEN_SIZE;

        fieldMetaArr[FIELD_INDEX_SUCCESS_DELETE_COUNT] =
                new FieldMeta("successDeleteCount", offset, FDFS_PROTO_PKG_LEN_SIZE);
        offset += FDFS_PROTO_PKG_LEN_SIZE;

        fieldMetaArr[FIELD_INDEX_TOTAL_DOWNLOAD_COUNT] =
                new FieldMeta("totalDownloadCount", offset, FDFS_PROTO_PKG_LEN_SIZE);
        offset += FDFS_PROTO_PKG_LEN_SIZE;

        fieldMetaArr[FIELD_INDEX_SUCCESS_DOWNLOAD_COUNT] =
                new FieldMeta("successDownloadCount", offset, FDFS_PROTO_PKG_LEN_SIZE);
        offset += FDFS_PROTO_PKG_LEN_SIZE;

        fieldMetaArr[FIELD_INDEX_TOTAL_GET_META_COUNT] =
                new FieldMeta("totalGetMetaCount", offset, FDFS_PROTO_PKG_LEN_SIZE);
        offset += FDFS_PROTO_PKG_LEN_SIZE;

        fieldMetaArr[FIELD_INDEX_SUCCESS_GET_META_COUNT] =
                new FieldMeta("successGetMetaCount", offset, FDFS_PROTO_PKG_LEN_SIZE);
        offset += FDFS_PROTO_PKG_LEN_SIZE;

        fieldMetaArr[FIELD_INDEX_TOTAL_CREATE_LINK_COUNT] =
                new FieldMeta("totalCreateLinkCount", offset, FDFS_PROTO_PKG_LEN_SIZE);
        offset += FDFS_PROTO_PKG_LEN_SIZE;

        fieldMetaArr[FIELD_INDEX_SUCCESS_CREATE_LINK_COUNT] =
                new FieldMeta("successCreateLinkCount", offset, FDFS_PROTO_PKG_LEN_SIZE);
        offset += FDFS_PROTO_PKG_LEN_SIZE;

        fieldMetaArr[FIELD_INDEX_TOTAL_DELETE_LINK_COUNT] =
                new FieldMeta("totalDeleteLinkCount", offset, FDFS_PROTO_PKG_LEN_SIZE);
        offset += FDFS_PROTO_PKG_LEN_SIZE;

        fieldMetaArr[FIELD_INDEX_SUCCESS_DELETE_LINK_COUNT] =
                new FieldMeta("successDeleteLinkCount", offset, FDFS_PROTO_PKG_LEN_SIZE);
        offset += FDFS_PROTO_PKG_LEN_SIZE;

        fieldMetaArr[FIELD_INDEX_TOTAL_UPLOAD_BYTES] =
                new FieldMeta("totalUploadBytes", offset, FDFS_PROTO_PKG_LEN_SIZE);
        offset += FDFS_PROTO_PKG_LEN_SIZE;

        fieldMetaArr[FIELD_INDEX_SUCCESS_UPLOAD_BYTES] =
                new FieldMeta("successUploadBytes", offset, FDFS_PROTO_PKG_LEN_SIZE);
        offset += FDFS_PROTO_PKG_LEN_SIZE;

        fieldMetaArr[FIELD_INDEX_TOTAL_APPEND_BYTES] =
                new FieldMeta("totalAppendBytes", offset, FDFS_PROTO_PKG_LEN_SIZE);
        offset += FDFS_PROTO_PKG_LEN_SIZE;

        fieldMetaArr[FIELD_INDEX_SUCCESS_APPEND_BYTES] =
                new FieldMeta("successAppendBytes", offset, FDFS_PROTO_PKG_LEN_SIZE);
        offset += FDFS_PROTO_PKG_LEN_SIZE;

        fieldMetaArr[FIELD_INDEX_TOTAL_MODIFY_BYTES] =
                new FieldMeta("totalModifyBytes", offset, FDFS_PROTO_PKG_LEN_SIZE);
        offset += FDFS_PROTO_PKG_LEN_SIZE;

        fieldMetaArr[FIELD_INDEX_SUCCESS_MODIFY_BYTES] =
                new FieldMeta("successModifyBytes", offset, FDFS_PROTO_PKG_LEN_SIZE);
        offset += FDFS_PROTO_PKG_LEN_SIZE;

        fieldMetaArr[FIELD_INDEX_TOTAL_DOWNLOAD_BYTES] =
                new FieldMeta("totalDownloadloadBytes", offset, FDFS_PROTO_PKG_LEN_SIZE);
        offset += FDFS_PROTO_PKG_LEN_SIZE;

        fieldMetaArr[FIELD_INDEX_SUCCESS_DOWNLOAD_BYTES] =
                new FieldMeta("successDownloadloadBytes", offset, FDFS_PROTO_PKG_LEN_SIZE);
        offset += FDFS_PROTO_PKG_LEN_SIZE;

        fieldMetaArr[FIELD_INDEX_TOTAL_SYNC_IN_BYTES] =
                new FieldMeta("totalSyncInBytes", offset, FDFS_PROTO_PKG_LEN_SIZE);
        offset += FDFS_PROTO_PKG_LEN_SIZE;

        fieldMetaArr[FIELD_INDEX_SUCCESS_SYNC_IN_BYTES] =
                new FieldMeta("successSyncInBytes", offset, FDFS_PROTO_PKG_LEN_SIZE);
        offset += FDFS_PROTO_PKG_LEN_SIZE;

        fieldMetaArr[FIELD_INDEX_TOTAL_SYNC_OUT_BYTES] =
                new FieldMeta("totalSyncOutBytes", offset, FDFS_PROTO_PKG_LEN_SIZE);
        offset += FDFS_PROTO_PKG_LEN_SIZE;

        fieldMetaArr[FIELD_INDEX_SUCCESS_SYNC_OUT_BYTES] =
                new FieldMeta("successSyncOutBytes", offset, FDFS_PROTO_PKG_LEN_SIZE);
        offset += FDFS_PROTO_PKG_LEN_SIZE;

        fieldMetaArr[FIELD_INDEX_TOTAL_FILE_OPEN_COUNT] =
                new FieldMeta("totalFileOpenCount", offset, FDFS_PROTO_PKG_LEN_SIZE);
        offset += FDFS_PROTO_PKG_LEN_SIZE;

        fieldMetaArr[FIELD_INDEX_SUCCESS_FILE_OPEN_COUNT] =
                new FieldMeta("successFileOpenCount", offset, FDFS_PROTO_PKG_LEN_SIZE);
        offset += FDFS_PROTO_PKG_LEN_SIZE;

        fieldMetaArr[FIELD_INDEX_TOTAL_FILE_READ_COUNT] =
                new FieldMeta("totalFileReadCount", offset, FDFS_PROTO_PKG_LEN_SIZE);
        offset += FDFS_PROTO_PKG_LEN_SIZE;

        fieldMetaArr[FIELD_INDEX_SUCCESS_FILE_READ_COUNT] =
                new FieldMeta("successFileReadCount", offset, FDFS_PROTO_PKG_LEN_SIZE);
        offset += FDFS_PROTO_PKG_LEN_SIZE;

        fieldMetaArr[FIELD_INDEX_TOTAL_FILE_WRITE_COUNT] =
                new FieldMeta("totalFileWriteCount", offset, FDFS_PROTO_PKG_LEN_SIZE);
        offset += FDFS_PROTO_PKG_LEN_SIZE;

        fieldMetaArr[FIELD_INDEX_SUCCESS_FILE_WRITE_COUNT] =
                new FieldMeta("successFileWriteCount", offset, FDFS_PROTO_PKG_LEN_SIZE);
        offset += FDFS_PROTO_PKG_LEN_SIZE;

        fieldMetaArr[FIELD_INDEX_LAST_SOURCE_UPDATE] =
                new FieldMeta("lastSourceUpdate", offset, FDFS_PROTO_PKG_LEN_SIZE);
        offset += FDFS_PROTO_PKG_LEN_SIZE;

        fieldMetaArr[FIELD_INDEX_LAST_SYNC_UPDATE] =
                new FieldMeta("lastSyncUpdate", offset, FDFS_PROTO_PKG_LEN_SIZE);
        offset += FDFS_PROTO_PKG_LEN_SIZE;

        fieldMetaArr[FIELD_INDEX_LAST_SYNCED_TIMESTAMP] =
                new FieldMeta("lastSyncedTimestamp", offset, FDFS_PROTO_PKG_LEN_SIZE);
        offset += FDFS_PROTO_PKG_LEN_SIZE;

        fieldMetaArr[FIELD_INDEX_LAST_HEART_BEAT_TIME] =
                new FieldMeta("lastHeartBeatTime", offset, FDFS_PROTO_PKG_LEN_SIZE);
        offset += FDFS_PROTO_PKG_LEN_SIZE;

        fieldMetaArr[FIELD_INDEX_IF_TRUNK_FILE] = new FieldMeta("ifTrunkServer", offset, 1);
        offset += 1;

        fieldsTotalSize = offset;
    }

    protected byte status;
    protected String id;
    protected String ipAddr;
    protected String srcIpAddr;
    protected String domainName; // http domain name
    protected String version;
    protected long totalMB; // total disk storage in MB
    protected long freeMB; // free disk storage in MB
    protected int uploadPriority; // upload priority
    protected Date joinTime; // storage join timestamp (create timestamp)
    protected Date upTime; // storage service started timestamp
    protected int storePathCount; // store base path count of each storage
                                  // server
    protected int subdirCountPerPath;
    protected int storagePort;
    protected int storageHttpPort; // storage http server port
    protected int currentWritePath; // current write path index
    protected long totalUploadCount;
    protected long successUploadCount;
    protected long totalAppendCount;
    protected long successAppendCount;
    protected long totalModifyCount;
    protected long successModifyCount;
    protected long totalTruncateCount;
    protected long successTruncateCount;
    protected long totalSetMetaCount;
    protected long successSetMetaCount;
    protected long totalDeleteCount;
    protected long successDeleteCount;
    protected long totalDownloadCount;
    protected long successDownloadCount;
    protected long totalGetMetaCount;
    protected long successGetMetaCount;
    protected long totalCreateLinkCount;
    protected long successCreateLinkCount;
    protected long totalDeleteLinkCount;
    protected long successDeleteLinkCount;
    protected long totalUploadBytes;
    protected long successUploadBytes;
    protected long totalAppendBytes;
    protected long successAppendBytes;
    protected long totalModifyBytes;
    protected long successModifyBytes;
    protected long totalDownloadloadBytes;
    protected long successDownloadloadBytes;
    protected long totalSyncInBytes;
    protected long successSyncInBytes;
    protected long totalSyncOutBytes;
    protected long successSyncOutBytes;
    protected long totalFileOpenCount;
    protected long successFileOpenCount;
    protected long totalFileReadCount;
    protected long successFileReadCount;
    protected long totalFileWriteCount;
    protected long successFileWriteCount;
    protected Date lastSourceUpdate;
    protected Date lastSyncUpdate;
    protected Date lastSyncedTimestamp;
    protected Date lastHeartBeatTime;
    protected boolean ifTrunkServer;


    public StorageStat(String charset) {
        super(charset);
    }


    /**
     * get storage status
     * 
     * @return storage status
     */
    public byte getStatus() {
        return status;
    }


    /**
     * get storage server id
     * 
     * @return storage server id
     */
    public String getId() {
        return id;
    }


    /**
     * get storage server ip address
     * 
     * @return storage server ip address
     */
    public String getIpAddr() {
        return ipAddr;
    }


    /**
     * get source storage ip address
     * 
     * @return source storage ip address
     */
    public String getSrcIpAddr() {
        return srcIpAddr;
    }


    /**
     * get the domain name of the storage server
     * 
     * @return the domain name of the storage server
     */
    public String getDomainName() {
        return domainName;
    }


    /**
     * get storage version
     * 
     * @return storage version
     */
    public String getVersion() {
        return version;
    }


    /**
     * get total disk space in MB
     * 
     * @return total disk space in MB
     */
    public long getTotalMB() {
        return totalMB;
    }


    /**
     * get free disk space in MB
     * 
     * @return free disk space in MB
     */
    public long getFreeMB() {
        return freeMB;
    }


    /**
     * get storage server upload priority
     * 
     * @return storage server upload priority
     */
    public int getUploadPriority() {
        return uploadPriority;
    }


    /**
     * get storage server join time
     * 
     * @return storage server join time
     */
    public Date getJoinTime() {
        return joinTime;
    }


    /**
     * get storage server up time
     * 
     * @return storage server up time
     */
    public Date getUpTime() {
        return upTime;
    }


    /**
     * get store base path count of each storage server
     * 
     * @return store base path count of each storage server
     */
    public int getStorePathCount() {
        return storePathCount;
    }


    /**
     * get sub dir count per store path
     * 
     * @return sub dir count per store path
     */
    public int getSubdirCountPerPath() {
        return subdirCountPerPath;
    }


    /**
     * get storage server port
     * 
     * @return storage server port
     */
    public int getStoragePort() {
        return storagePort;
    }


    /**
     * get storage server HTTP port
     * 
     * @return storage server HTTP port
     */
    public int getStorageHttpPort() {
        return storageHttpPort;
    }


    /**
     * get current write path index
     * 
     * @return current write path index
     */
    public int getCurrentWritePath() {
        return currentWritePath;
    }


    /**
     * get total upload file count
     * 
     * @return total upload file count
     */
    public long getTotalUploadCount() {
        return totalUploadCount;
    }


    /**
     * get success upload file count
     * 
     * @return success upload file count
     */
    public long getSuccessUploadCount() {
        return successUploadCount;
    }


    /**
     * get total append count
     * 
     * @return total append count
     */
    public long getTotalAppendCount() {
        return totalAppendCount;
    }


    /**
     * get success append count
     * 
     * @return success append count
     */
    public long getSuccessAppendCount() {
        return successAppendCount;
    }


    /**
     * get total modify count
     * 
     * @return total modify count
     */
    public long getTotalModifyCount() {
        return totalModifyCount;
    }


    /**
     * get success modify count
     * 
     * @return success modify count
     */
    public long getSuccessModifyCount() {
        return successModifyCount;
    }


    /**
     * get total truncate count
     * 
     * @return total truncate count
     */
    public long getTotalTruncateCount() {
        return totalTruncateCount;
    }


    /**
     * get success truncate count
     * 
     * @return success truncate count
     */
    public long getSuccessTruncateCount() {
        return successTruncateCount;
    }


    /**
     * get total set meta data count
     * 
     * @return total set meta data count
     */
    public long getTotalSetMetaCount() {
        return totalSetMetaCount;
    }


    /**
     * get success set meta data count
     * 
     * @return success set meta data count
     */
    public long getSuccessSetMetaCount() {
        return successSetMetaCount;
    }


    /**
     * get total delete file count
     * 
     * @return total delete file count
     */
    public long getTotalDeleteCount() {
        return totalDeleteCount;
    }


    /**
     * get success delete file count
     * 
     * @return success delete file count
     */
    public long getSuccessDeleteCount() {
        return successDeleteCount;
    }


    /**
     * get total download file count
     * 
     * @return total download file count
     */
    public long getTotalDownloadCount() {
        return totalDownloadCount;
    }


    /**
     * get success download file count
     * 
     * @return success download file count
     */
    public long getSuccessDownloadCount() {
        return successDownloadCount;
    }


    /**
     * get total get metadata count
     * 
     * @return total get metadata count
     */
    public long getTotalGetMetaCount() {
        return totalGetMetaCount;
    }


    /**
     * get success get metadata count
     * 
     * @return success get metadata count
     */
    public long getSuccessGetMetaCount() {
        return successGetMetaCount;
    }


    /**
     * get total create linke count
     * 
     * @return total create linke count
     */
    public long getTotalCreateLinkCount() {
        return totalCreateLinkCount;
    }


    /**
     * get success create linke count
     * 
     * @return success create linke count
     */
    public long getSuccessCreateLinkCount() {
        return successCreateLinkCount;
    }


    /**
     * get total delete link count
     * 
     * @return total delete link count
     */
    public long getTotalDeleteLinkCount() {
        return totalDeleteLinkCount;
    }


    /**
     * get success delete link count
     * 
     * @return success delete link count
     */
    public long getSuccessDeleteLinkCount() {
        return successDeleteLinkCount;
    }


    /**
     * get total upload file bytes
     * 
     * @return total upload file bytes
     */
    public long getTotalUploadBytes() {
        return totalUploadBytes;
    }


    /**
     * get success upload file bytes
     * 
     * @return success upload file bytes
     */
    public long getSuccessUploadBytes() {
        return successUploadBytes;
    }


    /**
     * get total append bytes
     * 
     * @return total append bytes
     */
    public long getTotalAppendBytes() {
        return totalAppendBytes;
    }


    /**
     * get success append bytes
     * 
     * @return success append bytes
     */
    public long getSuccessAppendBytes() {
        return successAppendBytes;
    }


    /**
     * get total modify bytes
     * 
     * @return total modify bytes
     */
    public long getTotalModifyBytes() {
        return totalModifyBytes;
    }


    /**
     * get success modify bytes
     * 
     * @return success modify bytes
     */
    public long getSuccessModifyBytes() {
        return successModifyBytes;
    }


    /**
     * get total download file bytes
     * 
     * @return total download file bytes
     */
    public long getTotalDownloadloadBytes() {
        return totalDownloadloadBytes;
    }


    /**
     * get success download file bytes
     * 
     * @return success download file bytes
     */
    public long getSuccessDownloadloadBytes() {
        return successDownloadloadBytes;
    }


    /**
     * get total sync in bytes
     * 
     * @return total sync in bytes
     */
    public long getTotalSyncInBytes() {
        return totalSyncInBytes;
    }


    /**
     * get success sync in bytes
     * 
     * @return success sync in bytes
     */
    public long getSuccessSyncInBytes() {
        return successSyncInBytes;
    }


    /**
     * get total sync out bytes
     * 
     * @return total sync out bytes
     */
    public long getTotalSyncOutBytes() {
        return totalSyncOutBytes;
    }


    /**
     * get success sync out bytes
     * 
     * @return success sync out bytes
     */
    public long getSuccessSyncOutBytes() {
        return successSyncOutBytes;
    }


    /**
     * get total file opened count
     * 
     * @return total file opened bytes
     */
    public long getTotalFileOpenCount() {
        return totalFileOpenCount;
    }


    /**
     * get success file opened count
     * 
     * @return success file opened count
     */
    public long getSuccessFileOpenCount() {
        return successFileOpenCount;
    }


    /**
     * get total file read count
     * 
     * @return total file read bytes
     */
    public long getTotalFileReadCount() {
        return totalFileReadCount;
    }


    /**
     * get success file read count
     * 
     * @return success file read count
     */
    public long getSuccessFileReadCount() {
        return successFileReadCount;
    }


    /**
     * get total file write count
     * 
     * @return total file write bytes
     */
    public long getTotalFileWriteCount() {
        return totalFileWriteCount;
    }


    /**
     * get success file write count
     * 
     * @return success file write count
     */
    public long getSuccessFileWriteCount() {
        return successFileWriteCount;
    }


    /**
     * get last source update timestamp
     * 
     * @return last source update timestamp
     */
    public Date getLastSourceUpdate() {
        return lastSourceUpdate;
    }


    /**
     * get last synced update timestamp
     * 
     * @return last synced update timestamp
     */
    public Date getLastSyncUpdate() {
        return lastSyncUpdate;
    }


    /**
     * get last synced timestamp
     * 
     * @return last synced timestamp
     */
    public Date getLastSyncedTimestamp() {
        return lastSyncedTimestamp;
    }


    /**
     * get last heart beat timestamp
     * 
     * @return last heart beat timestamp
     */
    public Date getLastHeartBeatTime() {
        return lastHeartBeatTime;
    }


    /**
     * if the trunk server
     * 
     * @return true for the trunk server, otherwise false
     */
    public boolean isTrunkServer() {
        return ifTrunkServer;
    }


    /**
     * set fields
     * 
     * @param bs
     *            byte array
     * @param offset
     *            start offset
     */
    public void setFields(byte[] bs, int offset) {
        this.status = readByte(bs, offset, fieldMetaArr[FIELD_INDEX_STATUS]);
        this.id = readString(bs, offset, fieldMetaArr[FIELD_INDEX_ID]);
        this.ipAddr = readString(bs, offset, fieldMetaArr[FIELD_INDEX_IP_ADDR]);
        this.srcIpAddr = readString(bs, offset, fieldMetaArr[FIELD_INDEX_SRC_IP_ADDR]);
        this.domainName = readString(bs, offset, fieldMetaArr[FIELD_INDEX_DOMAIN_NAME]);
        this.version = readString(bs, offset, fieldMetaArr[FIELD_INDEX_VERSION]);
        this.totalMB = readLong(bs, offset, fieldMetaArr[FIELD_INDEX_TOTAL_MB]);
        this.freeMB = readLong(bs, offset, fieldMetaArr[FIELD_INDEX_FREE_MB]);
        this.uploadPriority = readInt(bs, offset, fieldMetaArr[FIELD_INDEX_UPLOAD_PRIORITY]);
        this.joinTime = readDate(bs, offset, fieldMetaArr[FIELD_INDEX_JOIN_TIME]);
        this.upTime = readDate(bs, offset, fieldMetaArr[FIELD_INDEX_UP_TIME]);
        this.storePathCount = readInt(bs, offset, fieldMetaArr[FIELD_INDEX_STORE_PATH_COUNT]);
        this.subdirCountPerPath = readInt(bs, offset, fieldMetaArr[FIELD_INDEX_SUBDIR_COUNT_PER_PATH]);
        this.storagePort = readInt(bs, offset, fieldMetaArr[FIELD_INDEX_STORAGE_PORT]);
        this.storageHttpPort = readInt(bs, offset, fieldMetaArr[FIELD_INDEX_STORAGE_HTTP_PORT]);
        this.currentWritePath = readInt(bs, offset, fieldMetaArr[FIELD_INDEX_CURRENT_WRITE_PATH]);
        this.totalUploadCount = readLong(bs, offset, fieldMetaArr[FIELD_INDEX_TOTAL_UPLOAD_COUNT]);
        this.successUploadCount = readLong(bs, offset, fieldMetaArr[FIELD_INDEX_SUCCESS_UPLOAD_COUNT]);
        this.totalAppendCount = readLong(bs, offset, fieldMetaArr[FIELD_INDEX_TOTAL_APPEND_COUNT]);
        this.successAppendCount = readLong(bs, offset, fieldMetaArr[FIELD_INDEX_SUCCESS_APPEND_COUNT]);
        this.totalModifyCount = readLong(bs, offset, fieldMetaArr[FIELD_INDEX_TOTAL_MODIFY_COUNT]);
        this.successModifyCount = readLong(bs, offset, fieldMetaArr[FIELD_INDEX_SUCCESS_MODIFY_COUNT]);
        this.totalTruncateCount = readLong(bs, offset, fieldMetaArr[FIELD_INDEX_TOTAL_TRUNCATE_COUNT]);
        this.successTruncateCount = readLong(bs, offset, fieldMetaArr[FIELD_INDEX_SUCCESS_TRUNCATE_COUNT]);
        this.totalSetMetaCount = readLong(bs, offset, fieldMetaArr[FIELD_INDEX_TOTAL_SET_META_COUNT]);
        this.successSetMetaCount = readLong(bs, offset, fieldMetaArr[FIELD_INDEX_SUCCESS_SET_META_COUNT]);
        this.totalDeleteCount = readLong(bs, offset, fieldMetaArr[FIELD_INDEX_TOTAL_DELETE_COUNT]);
        this.successDeleteCount = readLong(bs, offset, fieldMetaArr[FIELD_INDEX_SUCCESS_DELETE_COUNT]);
        this.totalDownloadCount = readLong(bs, offset, fieldMetaArr[FIELD_INDEX_TOTAL_DOWNLOAD_COUNT]);
        this.successDownloadCount = readLong(bs, offset, fieldMetaArr[FIELD_INDEX_SUCCESS_DOWNLOAD_COUNT]);
        this.totalGetMetaCount = readLong(bs, offset, fieldMetaArr[FIELD_INDEX_TOTAL_GET_META_COUNT]);
        this.successGetMetaCount = readLong(bs, offset, fieldMetaArr[FIELD_INDEX_SUCCESS_GET_META_COUNT]);
        this.totalCreateLinkCount = readLong(bs, offset, fieldMetaArr[FIELD_INDEX_TOTAL_CREATE_LINK_COUNT]);
        this.successCreateLinkCount =
                readLong(bs, offset, fieldMetaArr[FIELD_INDEX_SUCCESS_CREATE_LINK_COUNT]);
        this.totalDeleteLinkCount = readLong(bs, offset, fieldMetaArr[FIELD_INDEX_TOTAL_DELETE_LINK_COUNT]);
        this.successDeleteLinkCount =
                readLong(bs, offset, fieldMetaArr[FIELD_INDEX_SUCCESS_DELETE_LINK_COUNT]);
        this.totalUploadBytes = readLong(bs, offset, fieldMetaArr[FIELD_INDEX_TOTAL_UPLOAD_BYTES]);
        this.successUploadBytes = readLong(bs, offset, fieldMetaArr[FIELD_INDEX_SUCCESS_UPLOAD_BYTES]);
        this.totalAppendBytes = readLong(bs, offset, fieldMetaArr[FIELD_INDEX_TOTAL_APPEND_BYTES]);
        this.successAppendBytes = readLong(bs, offset, fieldMetaArr[FIELD_INDEX_SUCCESS_APPEND_BYTES]);
        this.totalModifyBytes = readLong(bs, offset, fieldMetaArr[FIELD_INDEX_TOTAL_MODIFY_BYTES]);
        this.successModifyBytes = readLong(bs, offset, fieldMetaArr[FIELD_INDEX_SUCCESS_MODIFY_BYTES]);
        this.totalDownloadloadBytes = readLong(bs, offset, fieldMetaArr[FIELD_INDEX_TOTAL_DOWNLOAD_BYTES]);
        this.successDownloadloadBytes =
                readLong(bs, offset, fieldMetaArr[FIELD_INDEX_SUCCESS_DOWNLOAD_BYTES]);
        this.totalSyncInBytes = readLong(bs, offset, fieldMetaArr[FIELD_INDEX_TOTAL_SYNC_IN_BYTES]);
        this.successSyncInBytes = readLong(bs, offset, fieldMetaArr[FIELD_INDEX_SUCCESS_SYNC_IN_BYTES]);
        this.totalSyncOutBytes = readLong(bs, offset, fieldMetaArr[FIELD_INDEX_TOTAL_SYNC_OUT_BYTES]);
        this.successSyncOutBytes = readLong(bs, offset, fieldMetaArr[FIELD_INDEX_SUCCESS_SYNC_OUT_BYTES]);
        this.totalFileOpenCount = readLong(bs, offset, fieldMetaArr[FIELD_INDEX_TOTAL_FILE_OPEN_COUNT]);
        this.successFileOpenCount = readLong(bs, offset, fieldMetaArr[FIELD_INDEX_SUCCESS_FILE_OPEN_COUNT]);
        this.totalFileReadCount = readLong(bs, offset, fieldMetaArr[FIELD_INDEX_TOTAL_FILE_READ_COUNT]);
        this.successFileReadCount = readLong(bs, offset, fieldMetaArr[FIELD_INDEX_SUCCESS_FILE_READ_COUNT]);
        this.totalFileWriteCount = readLong(bs, offset, fieldMetaArr[FIELD_INDEX_TOTAL_FILE_WRITE_COUNT]);
        this.successFileWriteCount = readLong(bs, offset, fieldMetaArr[FIELD_INDEX_SUCCESS_FILE_WRITE_COUNT]);
        this.lastSourceUpdate = readDate(bs, offset, fieldMetaArr[FIELD_INDEX_LAST_SOURCE_UPDATE]);
        this.lastSyncUpdate = readDate(bs, offset, fieldMetaArr[FIELD_INDEX_LAST_SYNC_UPDATE]);
        this.lastSyncedTimestamp = readDate(bs, offset, fieldMetaArr[FIELD_INDEX_LAST_SYNCED_TIMESTAMP]);
        this.lastHeartBeatTime = readDate(bs, offset, fieldMetaArr[FIELD_INDEX_LAST_HEART_BEAT_TIME]);
        this.ifTrunkServer = readBoolean(bs, offset, fieldMetaArr[FIELD_INDEX_IF_TRUNK_FILE]);
    }


    /**
     * get fields total size
     * 
     * @return fields total size
     */
    public static int getFieldsTotalSize() {
        return fieldsTotalSize;
    }

}
