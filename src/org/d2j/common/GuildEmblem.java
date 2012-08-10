package org.d2j.common;

import org.d2j.utils.AbstractCacheSystem;
import org.d2j.utils.DelegateCacheSystem;
import org.d2j.utils.Maker;

import static org.d2j.common.StringUtils.toBase36;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 04/02/12
 * Time: 10:56
 * To change this template use File | Settings | File Templates.
 */
public class GuildEmblem implements Maker<String> {
    private int backgroundId;
    private int backgroundColor;
    private int foregroundId;
    private int foregroundColor;

    private AbstractCacheSystem<String> cache;

    public GuildEmblem(int backgroundId, int backgroundColor, int foregroundId, int foregroundColor) {
        this.backgroundId = backgroundId;
        this.backgroundColor = backgroundColor;
        this.foregroundId = foregroundId;
        this.foregroundColor = foregroundColor;

        this.cache = new DelegateCacheSystem<>(this);
    }

    public int getBackgroundId() {
        return backgroundId;
    }

    public void setBackgroundId(int backgroundId) {
        this.backgroundId = backgroundId;
        cache.setRefresh();
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        cache.setRefresh();
    }

    public int getForegroundId() {
        return foregroundId;
    }

    public void setForegroundId(int foregroundId) {
        this.foregroundId = foregroundId;
        cache.setRefresh();
    }

    public int getForegroundColor() {
        return foregroundColor;
    }

    public void setForegroundColor(int foregroundColor) {
        this.foregroundColor = foregroundColor;
        cache.setRefresh();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GuildEmblem that = (GuildEmblem) o;

        return backgroundColor  == that.backgroundColor &&
               backgroundId     == that.backgroundId &&
               foregroundColor  == that.foregroundColor &&
               foregroundId     == that.foregroundId;

    }

    @Override
    public int hashCode() {
        int result = backgroundId;
        result = 31 * result + backgroundColor;
        result = 31 * result + foregroundId;
        result = 31 * result + foregroundColor;
        return result;
    }

    @Override
    public String toString() {
        return cache.get();
    }

    @Override
    public String make() {
        return toBase36(backgroundId)    + "," +
               toBase36(backgroundColor) + "," +
               toBase36(foregroundId)    + "," +
               toBase36(foregroundColor);
    }
}
