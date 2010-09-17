package org.fusesource.hawtdispatch;

/**
 * Created by IntelliJ IDEA.
 * User: stepn
 * Date: 17.09.2010
 * Time: 17:12:34
 * To change this template use File | Settings | File Templates.
 */
public interface Link<O extends DispatchObject> {
    public void atObjectDo(Runnable runnable);
    public void withObjectDo(RunnableFactory<O> factory);
}
