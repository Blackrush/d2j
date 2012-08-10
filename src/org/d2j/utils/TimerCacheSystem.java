package org.d2j.utils;

import org.joda.time.Duration;
import org.joda.time.Instant;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 01/02/12
 * Time: 19:05
 * To change this template use File | Settings | File Templates.
 */
public abstract class TimerCacheSystem<T> extends AbstractCacheSystem<T> {
    private Instant last;
    private Duration delay;

    protected TimerCacheSystem(Duration delay) {
        this.delay = delay;
    }

    protected TimerCacheSystem(T obj, Duration delay) {
        super(obj);
        this.delay = delay;
    }

    @Override
    protected void beforeGet() {
        if (last == null || Instant.now().compareTo(last.plus(delay)) >= 0){
            refresh = true;
        }
    }

    @Override
    protected void onRefreshed() {
        last = new Instant();

        super.onRefreshed();
    }
}
