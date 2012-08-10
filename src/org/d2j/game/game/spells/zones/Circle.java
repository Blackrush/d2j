package org.d2j.game.game.spells.zones;

import org.d2j.common.client.protocol.enums.OrientationEnum;
import org.d2j.game.game.Cell;
import org.d2j.game.game.pathfinding.Pathfinding;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Blackrush
 * Date: 01/03/12
 * Time: 20:10
 */
public class Circle implements Zone {
    private static OrientationEnum[] ORIENTATIONS = new OrientationEnum[]{
            OrientationEnum.NORTH_EAST,
            OrientationEnum.SOUTH_EAST,
            OrientationEnum.SOUTH_WEST,
            OrientationEnum.NORTH_WEST
    };

    private int size;

    public Circle(int size) {
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
        List<T> result = new ArrayList<>();
        result.add(target);

        int remaining = size;

        for (OrientationEnum orientation : ORIENTATIONS){
            T last = Pathfinding.getCellByOrientation(mapWidth, mapHeight, cells, target, orientation);
            result.add(last);

            addCells(remaining, result, last, cells, mapWidth, mapHeight);
        }

        return result;
    }

    public <T extends Cell> void addCells(int remaining, List<T> result, T current, T[] cells, int mapWidth, int mapHeight){
        if (remaining <= 0) return;

        remaining--;

        for (OrientationEnum orientation : ORIENTATIONS){
            T last = Pathfinding.getCellByOrientation(mapWidth, mapHeight, cells, current, orientation);
            if (!result.contains(last)){
                result.add(last);
            }

            addCells(remaining, result, last, cells, mapWidth, mapHeight);
        }
    }
}
