package com.zoo.fdfs.support;

import com.zoo.fdfs.api.Connection;
import com.zoo.fdfs.common.Circle;


/**
 * 
 * 
 * @author yankai913@gmail.com
 * @date 2014-4-4
 */
public final class SimpleConnectionPool {

    private Circle<Connection> circleData;


    public SimpleConnectionPool(int elementSize, int queueCapacity) {
        this.circleData = new Circle<Connection>(elementSize, queueCapacity);
    }


    public void put(Connection connection) {
        circleData.put(connection);
    }


    public Connection get() {
        return circleData.get();
    }

}
