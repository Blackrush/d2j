package org.d2j.game.game.spells.zones;

import org.d2j.common.client.protocol.enums.OrientationEnum;
import org.d2j.game.game.Cell;
import org.d2j.game.game.pathfinding.Pathfinding;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Blackrush
 * Date: 01/03/12
 * Time: 20:19
 */
public class Line implements Zone {
    private int size;

    public Line(int size) {
        this.size = size;
    }

    @Override
    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public <T extends Cell> Collection<T> filter(T current, T target, T[] cells, int mapWidth, int mapHeight) {
        List<T> result = new ArrayList<>(size + 1);
        result.add(target);

        OrientationEnum orientation = Pathfinding.getOrientationFromPoints(current.getPosition(), target.getPosition());

        T last = target;
        for (int i = 0; i < size; ++i){
            try {
                last = Pathfinding.getCellByOrientation(mapWidth, mapHeight, cells, last, orientation);
            } catch (IndexOutOfBoundsException ignored) {
                break;
            }

            result.add(last);
        }

        return result;
    }
}
