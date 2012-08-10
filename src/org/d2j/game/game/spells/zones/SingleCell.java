package org.d2j.game.game.spells.zones;

import org.d2j.common.CollectionUtils;
import org.d2j.game.game.Cell;

import java.util.ArrayList;
import java.util.Collection;

import static org.d2j.common.CollectionUtils.newList;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 27/02/12
 * Time: 19:15
 * To change this template use File | Settings | File Templates.
 */
public class SingleCell implements Zone {
    public static final SingleCell INSTANCE = new SingleCell();

    @Override
    public <T extends Cell> Collection<T> filter(T current, T target, T[] cells, int mapWidth, int mapHeight) {
        return newList(target);
    }

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public void setSize(int size) {
    }
}
