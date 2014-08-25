package com.zoo.fdfs.common;

import java.util.Collection;


/**
 * 
 * @author yankai913@gmail.com
 * @date 2014-8-21
 */
public class Asserts {

    public static void assertStringBlank(String str, String msg) {
        if (Strings.isBlank(str)) {
            throw new IllegalStateException(msg);
        }
    }


    public static void assertCollectionBlank(Collection<?> collection, String msg) {
        if (collection == null || collection.isEmpty()) {
            throw new IllegalStateException(msg);
        }
    }

    
}
