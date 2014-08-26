package com.zoo.fdfs.support.client;

import java.util.Map;


import com.zoo.fdfs.api.FdfsException;
import com.zoo.fdfs.api.UploadCallback;


/**
 * 
 * @author yankai913@gmail.com
 * @date 2014-8-25
 */
public interface UploadClient {

    public String[] uploadFile(String localFileName, String fileExtName, Map<String, String> meta) throws FdfsException;


    public String[] uploadFile(byte[] fileBuff, int offset, int length, String fileExtName, Map<String, String> meta) throws FdfsException;


    public String[] uploadFile(String groupName, byte[] fileBuff, int offset, int length, String fileExtName, Map<String, String> meta) throws FdfsException;


    public String[] uploadFile(byte[] fileBuff, String fileExtName, Map<String, String> meta) throws FdfsException;


    public String[] uploadFile(String groupName, byte[] fileBuff, String fileExtName, Map<String, String> meta) throws FdfsException;


    public String[] uploadFile(String groupName, long fileSize, UploadCallback callback, String fileExtName, Map<String, String> meta) throws FdfsException;


    public String[] uploadFile(String groupName, String masterFileName, String prefixName, String localFileName, String fileExtName, Map<String, String> meta) throws FdfsException;


    public String[] uploadFile(String groupName, String masterFileName, String prefixName, byte[] fileBuff, String fileExtName, Map<String, String> meta) throws FdfsException;


    public String[] uploadFile(String groupName, String masterFileName, String prefixName, byte[] fileBuff, int offset, int length, String fileExtName, Map<String, String> meta) throws FdfsException;


    public String[] uploadFile(String groupName, String masterFileName, String prefixName, long fileSize, UploadCallback callback, String fileExtName, Map<String, String> meta) throws FdfsException;


    public String[] uploadAppenderFile(String localFileName, String fileExtName, Map<String, String> meta) throws FdfsException;


    public String[] uploadAppenderFile(byte[] fileBuff, int offset, int length, String fileExtName, Map<String, String> meta) throws FdfsException;


    public String[] uploadAppenderFile(String groupName, byte[] fileBuff, int offset, int length, String fileExtName, Map<String, String> meta) throws FdfsException;


    public String[] uploadAppenderFile(byte[] fileBuff, String fileExtName, Map<String, String> meta) throws FdfsException;


    public String[] uploadAppenderFile(String groupName, byte[] fileBuff, String fileExtName, Map<String, String> meta) throws FdfsException;


    public String[] uploadAppenderFile(String groupName, long fileSize, UploadCallback callback, String fileExtName, Map<String, String> meta) throws FdfsException;
}
