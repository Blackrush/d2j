package org.d2j.game.game.spells.zones;

import org.d2j.game.game.Cell;
import org.d2j.game.game.fights.FightCell;
import org.d2j.game.game.fights.IFighter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 27/02/12
 * Time: 19:13
 * To change this template use File | Settings | File Templates.
 */
public interface Zone {
    <T extends Cell> Collection<T> filter(T current, T target, T[] cells, int mapWidth, int mapHeight);

    int getSize();
    void setSize(int size);
}
