package com.zoo.fdfs.support;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 
 * @author yankai913@gmail.com
 * @date 2014-3-13
 */
public class FSocketPool {

    private final BlockingQueue<FSocket> queue;


    public FSocketPool(FSocket[] fSocketArr) {
        if (fSocketArr == null || fSocketArr.length == 0) {
            throw new IllegalArgumentException("fSocketArr invalid");
        }
        this.queue = new LinkedBlockingQueue<FSocket>(fSocketArr.length);
        for (int i = 0; i < fSocketArr.length; i++) {
            try {
                this.queue.put(fSocketArr[i]);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public FSocket acquire() {
        try {
            return queue.take();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void release(FSocket t) {
        try {
            this.queue.put(t);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
