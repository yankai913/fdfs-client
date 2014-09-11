package com.zoo.fdfs.common;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;


/**
 * Circle Queue
 * 
 * @author yankai913@gmail.com
 * @date 2014-6-4
 */
public class Circle<E> {

    private final List<ArrayBlockingQueue<E>> data;

    private EffectiveInteger readSeq = new EffectiveInteger(0);

    private EffectiveInteger writeSeq = new EffectiveInteger(0);

    private int elementSize;

    private int queueCapacity;

    private int circleSize;


    public Circle(int elementSize, int queueCapacity) {
        if (elementSize < 0 || queueCapacity < 0) {
            throw new IllegalArgumentException("invalid number");
        }
        if (elementSize < queueCapacity) {
            throw new IllegalArgumentException("invalid elementLength < queueCapacity");
        }
        this.elementSize = elementSize;
        this.queueCapacity = queueCapacity;
        if (this.elementSize % this.queueCapacity == 0) {
            this.circleSize = this.elementSize / this.queueCapacity;
        }
        else {
            this.circleSize = this.elementSize / this.queueCapacity + 1;
        }

        this.data = new ArrayList<ArrayBlockingQueue<E>>(getCircleSize());
        for (int i = 0; i < getCircleSize(); i++) {
            this.data.add(new ArrayBlockingQueue<E>(getQueueCapacity()));
        }
    }


    public int getCircleSize() {
        return circleSize;
    }


    public int getElementSize() {
        return elementSize;
    }


    public int getQueueCapacity() {
        return queueCapacity;
    }


    public E get() {
        E e = null;
        for (;;) {
            int idx = readSeq.incrementAndGet() % getCircleSize();
            ArrayBlockingQueue<E> queue = data.get(idx);
            try {
                if (queue.peek() != null) {
                    e = queue.poll(5, TimeUnit.MILLISECONDS);
                    if (e != null) {
                        break;
                    }
                }
            } catch (Exception ex) {
            }
        }
        return e;
    }


    public void put(E e) {
        for (;;) {
            int idx = writeSeq.incrementAndGet() % getCircleSize();
            ArrayBlockingQueue<E> queue = data.get(idx);
            try {
                if (queue.offer(e)) {
                    break;
                }
            } catch (Exception ex) {
            }
        }
    }

}
