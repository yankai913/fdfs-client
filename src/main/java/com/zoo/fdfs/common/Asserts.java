package com.zoo.fdfs.common;

import java.util.Collection;


/**
 * 
 * @author yankai913@gmail.com
 * @date 2014-8-21
 */
public class Asserts {

    public static void assertStringIsBlank(String str, String msg) {
        if (Strings.isBlank(str)) {
            throw new IllegalStateException(msg);
        }
    }


    public static void assertCollectionIsBlank(Collection<?> collection, String msg) {
        if (collection == null || collection.isEmpty()) {
            throw new IllegalStateException(msg);
        }
    }


    public static void assertNull(Object obj, String msg) {
        if (obj == null) {
            throw new NullPointerException(msg);
        }
    }

}
