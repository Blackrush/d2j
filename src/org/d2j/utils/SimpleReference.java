package org.d2j.utils;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 04/02/12
 * Time: 13:23
 * To change this template use File | Settings | File Templates.
 */
public class SimpleReference<T> implements Reference<T> {
    T obj;

    public SimpleReference() {
    }

    public SimpleReference(T obj) {
        this.obj = obj;
    }

    @Override
    public T get() {
        return obj;
    }

    @Override
    public boolean isNull() {
        return obj == null;
    }

    @Override
    public void set(T obj) {
        this.obj = obj;
    }

    @Override
    public T getAndSet(T obj) {
        set(obj);
        return get();
    }
}
