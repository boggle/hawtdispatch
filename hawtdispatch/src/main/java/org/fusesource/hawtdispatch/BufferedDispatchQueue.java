package org.fusesource.hawtdispatch;

import org.fusesource.hawtdispatch.internal.AbstractDispatchObject;

import java.util.Queue;
import java.util.concurrent.TimeUnit;

/**
 * Created by IntelliJ IDEA.
 * User: stepn
 * Date: 17.09.2010
 * Time: 17:39:03
 * To change this template use File | Settings | File Templates.
 */
public class BufferedDispatchQueue extends AbstractDispatchObject implements DispatchQueue {
    private final Queue<DoWithQueue> buffer;

    public BufferedDispatchQueue(final Queue<DoWithQueue> aBuffer) {
        buffer = aBuffer;
    }

    public QueueType getQueueType() {
        return getTargetQueue().getQueueType();
    }

    public DispatchQueue createSerialQueue(String label) {
        return getTargetQueue().createSerialQueue(label);
    }

    public String getLabel() {
        return getTargetQueue().getLabel(); 
    }

    public boolean isExecuting() {
        return getTargetQueue().isExecuting();
    }

    public void execute(final Runnable runnable) {
        buffer.offer(new DoWithQueue() {
            public void doWithQueue(final DispatchQueue in) {
                in.execute(runnable);
            }
        });
    }

    public void dispatchAsync(final Runnable runnable) {
        buffer.offer(new DoWithQueue() {
            public void doWithQueue(final DispatchQueue in) {
                in.dispatchAsync(runnable);
            }
        });
    }

    public void dispatchAfter(final long delay, final TimeUnit unit, final Runnable runnable) {
        buffer.offer(new DoWithQueue() {
            public void doWithQueue(final DispatchQueue in) {
                in.dispatchAfter(delay, unit, runnable);
            }
        });
    }

    public void flushTo(int limit, final DispatchQueue targetQueue) {
        if (limit == 0)
            while (! buffer.isEmpty())
                buffer.remove().doWithQueue(targetQueue);
        else
            for (; limit > 0 && (! buffer.isEmpty()); limit--)
                buffer.remove().doWithQueue(targetQueue);
    }

    final public void flush(int limit) {
        flushTo(limit, getTargetQueue());
    }
    
    public interface DoWithQueue {
        public void doWithQueue(DispatchQueue queue);
    }
}
