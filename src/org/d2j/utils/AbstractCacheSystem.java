package org.d2j.utils;

import org.joda.time.Duration;
import org.joda.time.Instant;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 21/01/12
 * Time: 12:00
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractCacheSystem<T> implements Reference<T> {
    protected T obj;
    protected boolean refresh;

    protected AbstractCacheSystem() {
        this.refresh = true;
    }

    protected AbstractCacheSystem(T obj) {
        this.obj = obj;
        this.refresh = false;
    }

    protected abstract void refresh();

    protected void onRefreshed(){
        refresh = false;
    }

    protected void beforeGet(){

    }

    @Override
    public T get(){
        beforeGet();
        if (refresh){
            refresh();
            onRefreshed();
        }
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
        this.obj = obj;
        return obj;
    }

    public void setRefresh(){
        this.refresh = true;
    }
}
