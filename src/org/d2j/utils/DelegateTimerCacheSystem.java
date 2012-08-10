package org.d2j.utils;

import org.joda.time.Duration;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 21/01/12
 * Time: 12:06
 * To change this template use File | Settings | File Templates.
 */
public class DelegateTimerCacheSystem<T> extends TimerCacheSystem<T> {
    private final Maker<T> refresher;

    public DelegateTimerCacheSystem(Maker<T> refresher, Duration refreshDelay) {
        super(refreshDelay);
        this.refresher = refresher;
    }

    @Override
    protected void refresh() {
        obj = refresher.make();
    }
}
