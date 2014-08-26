package com.zoo.fdfs.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.zoo.fdfs.api.FdfsException;


/**
 * 
 * @author yankai913@gmail.com
 * @date 2014-8-21
 */
public class IOs {

    public static void close(InputStream is) {
        if (is != null) {
            try {
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public static void close(OutputStream os) {
        if (os != null) {
            try {
                os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public static void close(InputStream is, OutputStream os) {
        close(is);
        close(os);
    }


    public static byte[] getFileData(String localFileName) throws FdfsException {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(localFileName);
            byte[] data = new byte[fis.available()];
            fis.read(data);
            return data;
        } catch (Exception e) {
            throw new FdfsException(e.getMessage(), e);
        } finally {
            close(fis);
        }
    }


    public static FileInputStream getFileInputStream(String localFileName) throws FdfsException {
        try {
            return new FileInputStream(localFileName);
        } catch (Exception e) {
            throw new FdfsException(e.getMessage(), e);
        }
    }


    public static File getFile(String localFileName) throws FdfsException {
        try {
            return new File(localFileName);
        } catch (Exception e) {
            throw new FdfsException(e.getMessage(), e);
        }
    }


    public static FileInputStream getFileInputStream(File file) throws FdfsException {
        try {
            return new FileInputStream(file);
        } catch (Exception e) {
            throw new FdfsException(e.getMessage(), e);
        }
    }


    public static void writeToLocalFile(byte[] srcData, String localFileName) throws FdfsException {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(localFileName);
            fos.write(srcData);
        } catch (Exception e) {
            throw new FdfsException(e.getMessage(), e);
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
