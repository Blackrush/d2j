package org.d2j.utils;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 01/02/12
 * Time: 17:41
 * To change this template use File | Settings | File Templates.
 */
public class LazyLoad<T> extends AbstractLazyLoad<T> {
    private Maker<T> refresher;

    public LazyLoad(Maker<T> refresher) {
        this.refresher = refresher;
    }

    @Override
    protected T refresh() {
        return refresher.make();
    }
}
