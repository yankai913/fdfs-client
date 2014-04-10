package com.zoo.fdfs.support;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import com.zoo.fdfs.api.Connection;


/**
 * 环形队列，queueCount = poolSize / 5;
 * 
 * @author yankai913@gmail.com
 * @date 2014-4-4
 */
public class SimpleConnectionPool {

    private int queueCount;

    private int poolSize;

    private AtomicInteger putSeq = new AtomicInteger(0);

    private AtomicInteger getSeq = new AtomicInteger(0);

    private List<LinkedBlockingQueue<Connection>> queueList;


    public SimpleConnectionPool(int poolSize) {
        this.poolSize = poolSize;
        this.queueCount = this.poolSize / 5;
        List<LinkedBlockingQueue<Connection>> list =
                new ArrayList<LinkedBlockingQueue<Connection>>(getQueueCount());
        for (int i = 0; i < getQueueCount(); i++) {
            list.add(new LinkedBlockingQueue<Connection>());
        }
        this.queueList = Collections.unmodifiableList(list);
    }


    public int getQueueCount() {
        return this.queueCount;
    }


    public void put(Connection connection) {
        int index = putSeq.incrementAndGet() % getQueueCount();
        if (index < 0) {
            index = Math.abs(index);
        }
        try {
            queueList.get(index).put(connection);
        }
        catch (InterruptedException e) {
        }
    }


    public Connection get() {
        for (int i = 0; i < getQueueCount(); i++) {
            int index = getSeq.incrementAndGet() % getQueueCount();
            if (index < 0) {
                index = Math.abs(index);
            }
            try {
                Connection connection = queueList.get(index).poll(100, TimeUnit.MILLISECONDS);
                if (connection != null) {
                    return connection;
                }
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
