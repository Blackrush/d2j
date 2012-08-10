package org.d2j.utils;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 01/02/12
 * Time: 19:14
 * To change this template use File | Settings | File Templates.
 */
public class DelegateCacheSystem<T> extends AbstractCacheSystem<T> {
    private Maker<T> maker;

    public DelegateCacheSystem() {
    }

    public DelegateCacheSystem(Maker<T> maker) {
        this.maker = maker;
    }

    public DelegateCacheSystem(T obj, Maker<T> maker) {
        super(obj);
        this.maker = maker;
    }

    @Override
    protected void refresh() {
        obj = maker.make();
    }

    public Maker<T> getMaker() {
        return maker;
    }

    public void setMaker(Maker<T> maker) {
        this.maker = maker;
    }
}
