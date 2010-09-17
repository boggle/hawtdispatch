package org.fusesource.hawtdispatch;

import org.fusesource.hawtdispatch.internal.AbstractDispatchObject;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by IntelliJ IDEA.
 * User: stepn
 * Date: 17.09.2010
 * Time: 19:17:20
 * To change this template use File | Settings | File Templates.
 */
public class RecurringBarrierSource extends AbstractDispatchObject implements CustomDispatchSource<Integer, Boolean> {

    private final BufferedDispatchQueue[] objects;
    private final AtomicInteger[] counts;
    private Runnable evHandler;

    public RecurringBarrierSource(BufferedDispatchQueue[] objects) {
        this.objects = objects;
        counts = new AtomicInteger[objects.length];
        for (int i = 0; i < objects.length; i++)
            counts[i] = new AtomicInteger(0);
        setEventHandler(tick);
    }

    public Boolean getData() {
        for (int i = 0; i < objects.length; i++) {
            if (counts[i].get() == 0)
                return false;
        }
        return true;
    }

    public void merge(Integer value) {
        counts[value].incrementAndGet();
    }

    public final Runnable tick = new Runnable() {
        public void run() {
            final DispatchQueue targetQueue = getTargetQueue();
            targetQueue.retain();
            try {
                if (getData()) {
                    for (int i = 0; i < objects.length; i++) {
                        try {
                            objects[i].flushTo(1, targetQueue);
                        }
                        finally {
                            counts[i].decrementAndGet();
                        }
                    }
                }
                if (evHandler != null)
                    getTargetQueue().execute(evHandler);
            }
            finally { targetQueue.release(); }
        }
    };

    public void setCancelHandler(Runnable handler) {
        throw new UnsupportedOperationException();
    }

    public void setEventHandler(Runnable handler) {
        evHandler = handler;
    }

    public void cancel() {
        throw new UnsupportedOperationException();
    }

    public boolean isCanceled() {
        return false;
    }
}
