package com.zoo.fdfs.api;

import static com.zoo.fdfs.api.Constants.FDFS_GROUP_NAME_MAX_LEN;
import static com.zoo.fdfs.api.Constants.FDFS_PROTO_PKG_LEN_SIZE;


/**
 * 
 * @author yankai913@gmail.com
 * @date 2014-3-17
 */
public class GroupStat extends BaseStat {

    protected static final int FIELD_INDEX_GROUP_NAME = 0;
    protected static final int FIELD_INDEX_TOTAL_MB = 1;
    protected static final int FIELD_INDEX_FREE_MB = 2;
    protected static final int FIELD_INDEX_TRUNK_FREE_MB = 3;
    protected static final int FIELD_INDEX_STORAGE_COUNT = 4;
    protected static final int FIELD_INDEX_STORAGE_PORT = 5;
    protected static final int FIELD_INDEX_STORAGE_HTTP_PORT = 6;
    protected static final int FIELD_INDEX_ACTIVE_COUNT = 7;
    protected static final int FIELD_INDEX_CURRENT_WRITE_SERVER = 8;
    protected static final int FIELD_INDEX_STORE_PATH_COUNT = 9;
    protected static final int FIELD_INDEX_SUBDIR_COUNT_PER_PATH = 10;
    protected static final int FIELD_INDEX_CURRENT_TRUNK_FILE_ID = 11;

    protected static FieldMeta[] fieldMetaArr = new FieldMeta[12];
    protected static int fieldsTotalSize;

    static {
        int offset = 0;
        fieldMetaArr[FIELD_INDEX_GROUP_NAME] =
                new FieldMeta("groupName", offset, FDFS_GROUP_NAME_MAX_LEN + 1);
        offset += FDFS_GROUP_NAME_MAX_LEN + 1;

        fieldMetaArr[FIELD_INDEX_TOTAL_MB] = new FieldMeta("totalMB", offset, FDFS_PROTO_PKG_LEN_SIZE);
        offset += FDFS_PROTO_PKG_LEN_SIZE;

        fieldMetaArr[FIELD_INDEX_FREE_MB] = new FieldMeta("freeMB", offset, FDFS_PROTO_PKG_LEN_SIZE);
        offset += FDFS_PROTO_PKG_LEN_SIZE;

        fieldMetaArr[FIELD_INDEX_TRUNK_FREE_MB] =
                new FieldMeta("trunkFreeMB", offset, FDFS_PROTO_PKG_LEN_SIZE);
        offset += FDFS_PROTO_PKG_LEN_SIZE;

        fieldMetaArr[FIELD_INDEX_STORAGE_COUNT] =
                new FieldMeta("storageCount", offset, FDFS_PROTO_PKG_LEN_SIZE);
        offset += FDFS_PROTO_PKG_LEN_SIZE;

        fieldMetaArr[FIELD_INDEX_STORAGE_PORT] =
                new FieldMeta("storagePort", offset, FDFS_PROTO_PKG_LEN_SIZE);
        offset += FDFS_PROTO_PKG_LEN_SIZE;

        fieldMetaArr[FIELD_INDEX_STORAGE_HTTP_PORT] =
                new FieldMeta("storageHttpPort", offset, FDFS_PROTO_PKG_LEN_SIZE);
        offset += FDFS_PROTO_PKG_LEN_SIZE;

        fieldMetaArr[FIELD_INDEX_ACTIVE_COUNT] =
                new FieldMeta("activeCount", offset, FDFS_PROTO_PKG_LEN_SIZE);
        offset += FDFS_PROTO_PKG_LEN_SIZE;

        fieldMetaArr[FIELD_INDEX_CURRENT_WRITE_SERVER] =
                new FieldMeta("currentWriteServer", offset, FDFS_PROTO_PKG_LEN_SIZE);
        offset += FDFS_PROTO_PKG_LEN_SIZE;

        fieldMetaArr[FIELD_INDEX_STORE_PATH_COUNT] =
                new FieldMeta("storePathCount", offset, FDFS_PROTO_PKG_LEN_SIZE);
        offset += FDFS_PROTO_PKG_LEN_SIZE;

        fieldMetaArr[FIELD_INDEX_SUBDIR_COUNT_PER_PATH] =
                new FieldMeta("subdirCountPerPath", offset, FDFS_PROTO_PKG_LEN_SIZE);
        offset += FDFS_PROTO_PKG_LEN_SIZE;

        fieldMetaArr[FIELD_INDEX_CURRENT_TRUNK_FILE_ID] =
                new FieldMeta("currentTrunkFileId", offset, FDFS_PROTO_PKG_LEN_SIZE);
        offset += FDFS_PROTO_PKG_LEN_SIZE;

        fieldsTotalSize = offset;
    }


    public GroupStat(String charset) {
        super(charset);
    }

    /** name of this group */
    protected String groupName;

    /** total disk storage in MB */
    protected long totalMB;

    /** free disk space in MB */
    protected long freeMB;

    /** trunk free space in MB */
    protected long trunkFreeMB;

    /** storage server count */
    protected int storageCount;

    /** storage server port */
    protected int storagePort;

    /** storage server HTTP port */
    protected int storageHttpPort;

    /** active storage server count */
    protected int activeCount;

    /** current storage server index to upload file */
    protected int currentWriteServer;

    /** store base path count of each storage server */
    protected int storePathCount;

    /** sub dir count per store path */
    protected int subdirCountPerPath;

    /** current trunk file id */
    protected int currentTrunkFileId;


    @Override
    public void setFields(byte[] bs, int offset) {
        this.groupName = readString(bs, offset, fieldMetaArr[FIELD_INDEX_GROUP_NAME]);
        this.totalMB = readLong(bs, offset, fieldMetaArr[FIELD_INDEX_TOTAL_MB]);
        this.freeMB = readLong(bs, offset, fieldMetaArr[FIELD_INDEX_FREE_MB]);
        this.trunkFreeMB = readLong(bs, offset, fieldMetaArr[FIELD_INDEX_TRUNK_FREE_MB]);
        this.storageCount = readInt(bs, offset, fieldMetaArr[FIELD_INDEX_STORAGE_COUNT]);
        this.storagePort = readInt(bs, offset, fieldMetaArr[FIELD_INDEX_STORAGE_PORT]);
        this.storageHttpPort = readInt(bs, offset, fieldMetaArr[FIELD_INDEX_STORAGE_HTTP_PORT]);
        this.activeCount = readInt(bs, offset, fieldMetaArr[FIELD_INDEX_ACTIVE_COUNT]);
        this.currentWriteServer = readInt(bs, offset, fieldMetaArr[FIELD_INDEX_CURRENT_WRITE_SERVER]);
        this.storePathCount = readInt(bs, offset, fieldMetaArr[FIELD_INDEX_STORE_PATH_COUNT]);
        this.subdirCountPerPath = readInt(bs, offset, fieldMetaArr[FIELD_INDEX_SUBDIR_COUNT_PER_PATH]);
        this.currentTrunkFileId = readInt(bs, offset, fieldMetaArr[FIELD_INDEX_CURRENT_TRUNK_FILE_ID]);
    }


    public static FieldMeta[] getFieldMetaArr() {
        return fieldMetaArr;
    }


    public static int getFieldsTotalSize() {
        return fieldsTotalSize;
    }


    public String getGroupName() {
        return groupName;
    }


    public long getTotalMB() {
        return totalMB;
    }


    public long getFreeMB() {
        return freeMB;
    }


    public long getTrunkFreeMB() {
        return trunkFreeMB;
    }


    public int getStorageCount() {
        return storageCount;
    }


    public int getStoragePort() {
        return storagePort;
    }


    public int getStorageHttpPort() {
        return storageHttpPort;
    }


    public int getActiveCount() {
        return activeCount;
    }


    public int getCurrentWriteServer() {
        return currentWriteServer;
    }


    public int getStorePathCount() {
        return storePathCount;
    }


    public int getSubdirCountPerPath() {
        return subdirCountPerPath;
    }


    public int getCurrentTrunkFileId() {
        return currentTrunkFileId;
    }

}
