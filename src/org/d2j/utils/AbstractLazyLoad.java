package org.d2j.utils;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 11/02/12
 * Time: 17:32
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractLazyLoad<T> implements Reference<T> {
    private T obj;
    private boolean refresh = true;

    protected abstract T refresh();

    @Override
    public T get(){
        if (refresh){
            obj = refresh();
            refresh = false;
        }
        return obj;
    }

    @Override
    public void set(T obj){
        this.obj = obj;
        this.refresh = false;
    }

    @Override
    public T getAndSet(T obj) {
        set(obj);
        return get();
    }

    @Override
    public boolean isNull() {
        return obj == null;
    }
}
