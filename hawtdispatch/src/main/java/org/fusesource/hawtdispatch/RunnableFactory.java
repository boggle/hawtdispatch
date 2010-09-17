package org.fusesource.hawtdispatch;

/**
 * Created by IntelliJ IDEA.
 * User: stepn
 * Date: 17.09.2010
 * Time: 17:25:37
 * To change this template use File | Settings | File Templates.
 */
public interface RunnableFactory<I> {
    public Runnable createRunnableFor(I in);
}
