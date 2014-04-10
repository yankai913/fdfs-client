package com.zoo.fdfs.common;

import java.util.Collection;

/**
 * 
 * @author yankai913@gmail.com
 * @date 2014-4-10
 */
public class Collections {

    public static boolean isNotEmpty(Collection<?> collection) {
        if (collection != null && collection.size() > 0 ) {
            return true;
        }
        return false;
    }
}
