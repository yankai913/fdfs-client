package com.zoo.fdfs.common;

import java.util.concurrent.atomic.AtomicInteger;


/**
 * 
 * @author yankai913@gmail.com
 * @date 2014-9-11
 */
public final class EffectiveInteger {

    private final AtomicInteger i;


    public EffectiveInteger() {
        i = new AtomicInteger();
    }


    public EffectiveInteger(int initialValue) {
        i = new AtomicInteger(initialValue);
    }


    public final int incrementAndGet() {
        for (;;) {
            int current = i.get();
            int next = (current >= Integer.MAX_VALUE ? 0 : current + 1);
            if (i.compareAndSet(current, next)) {
                return next;
            }
        }
    }


    public final int decrementAndGet() {
        for (;;) {
            int current = i.get();
            int next = (current <= 0 ? Integer.MAX_VALUE : current - 1);
            if (i.compareAndSet(current, next)) {
                return next;
            }
        }
    }
}
