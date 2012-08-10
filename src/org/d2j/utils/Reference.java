package org.d2j.utils;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 04/02/12
 * Time: 13:22
 * To change this template use File | Settings | File Templates.
 */
public interface Reference<T> {
    T get();
    void set(T obj);
    T getAndSet(T obj);
    boolean isNull();
}
