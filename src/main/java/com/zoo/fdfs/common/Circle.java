package com.zoo.fdfs.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * 
 * @author yankai913@gmail.com
 * @date 2014-6-4
 */
public final class Circle<E> {

    private final List<E> data;

    private AtomicInteger readSeq = new AtomicInteger(0);

    private AtomicInteger writeSeq = new AtomicInteger(0);


    public Circle(Collection<E> collection) {
        this.data = new ArrayList<E>(collection);
    }


    public Circle(E[] data) {
        this.data = Arrays.asList(data);
    }


    public E readNext() {
        int idx = readSeq.incrementAndGet() % getCapacity();
        if (idx < 0) {
            idx = Math.abs(idx);
        }
        E e = data.get(idx);
        return e;
    }


    public E writeNext() {
        int idx = writeSeq.incrementAndGet() % getCapacity();
        if (idx < 0) {
            idx = Math.abs(idx);
        }
        E e = data.get(idx);
        return e;
    }


    public List<E> getData() {
        return data;
    }


    public int getCapacity() {
        return data.size();
    }

}
