package com.zoo.fdfs.common;

import com.zoo.fdfs.api.FdfsException;


/**
 * 
 * @author yankai913@gmail.com
 * @date 2014-3-17
 */
public class Strings {

    public static boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(str.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }


    public static byte[] getBytes(String str, String charsetName) throws FdfsException {
        if (isBlank(str)) {
            return null;
        }
        try {
            return str.getBytes(charsetName);
        } catch (Exception e) {
            throw new FdfsException(e.getMessage(), e);
        }
    }


    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }


    public static String format(String msg, Object... args) {
        int idx = msg.indexOf("{}");
        int j = 0;
        while (idx >= 0) {
            msg = msg.substring(0, idx) + args[j] + msg.substring(idx + 2);
            j = j + 1;
            idx = msg.indexOf("{}");
        }
        return msg;
    }
}
