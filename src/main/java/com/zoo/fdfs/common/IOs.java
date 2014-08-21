package com.zoo.fdfs.common;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;


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


    public static byte[] getFileData(String localFileName) {
        if (Strings.isBlank(localFileName)) {
            return null;
        }
        FileInputStream fis = null;
        byte[] data = null;
        try {

            fis = new FileInputStream(localFileName);
            data = new byte[fis.available()];
            fis.read(data);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(fis);
        }
        return data;
    }

}
