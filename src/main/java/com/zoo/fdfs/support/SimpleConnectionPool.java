package com.zoo.fdfs.support;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.zoo.fdfs.api.Connection;
import com.zoo.fdfs.common.Circle;


/**
 * 环形队列，queueCount = poolSize / 5;
 * 
 * @author yankai913@gmail.com
 * @date 2014-4-4
 */
public class SimpleConnectionPool {

    private int elementLength;// 池里所有元素的总个数

    private int circleCapacity = 5;// 默认设置环形数据结构里容量为5，有5个槽

    private Circle<LinkedBlockingQueue<Connection>> circleData;


    @SuppressWarnings("unchecked")
    public SimpleConnectionPool(int elementLength, int circleCapacity) {
        this.elementLength = elementLength;
        this.circleCapacity = circleCapacity;
        List<LinkedBlockingQueue<Connection>> data =
                new ArrayList<LinkedBlockingQueue<Connection>>(this.circleCapacity);
        this.circleData =
                new Circle<LinkedBlockingQueue<Connection>>(
                    (LinkedBlockingQueue<Connection>[]) data.toArray());
    }


    public int getElementLength() {
        return elementLength;
    }


    public int getCircleCapacity() {
        return circleCapacity;
    }


    public void put(Connection connection) {

        try {
            circleData.writeNext().put(connection);
        }
        catch (InterruptedException e) {
        }
    }


    public Connection get() {

        try {
            Connection connection = circleData.readNext().poll(100, TimeUnit.MILLISECONDS);
            if (connection != null) {
                return connection;
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

}