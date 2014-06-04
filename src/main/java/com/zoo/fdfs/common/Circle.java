package com.zoo.fdfs.common;

import java.util.concurrent.atomic.AtomicInteger;


/**
 * 
 * @author yankai913@gmail.com
 * @date 2014-6-4
 */
public final class Circle<E> {

    private E[] data;

    private AtomicInteger readSeq = new AtomicInteger(0);

    private AtomicInteger writeSeq = new AtomicInteger(0);


    public Circle(E[] data) {
        this.data = data;
    }


    public E readNext() {
        int idx = readSeq.incrementAndGet() % getCapacity();
        if (idx < 0) {
            idx = Math.abs(idx);
        }
        E e = data[idx];
        return e;
    }


    public E writeNext() {
        int idx = writeSeq.incrementAndGet() % getCapacity();
        if (idx < 0) {
            idx = Math.abs(idx);
        }
        E e = data[idx];
        return e;
    }


    public Object[] getData() {
        return data;
    }


    public int getCapacity() {
        return data.length;
    }

}
