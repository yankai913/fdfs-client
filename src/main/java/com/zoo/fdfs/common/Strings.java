package com.zoo.fdfs.common;

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


    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }
}
