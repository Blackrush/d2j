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
 * Time: 20:17
 */
public class Cross implements Zone {
    private static OrientationEnum[] ORIENTATIONS = new OrientationEnum[]{
            OrientationEnum.NORTH_EAST,
            OrientationEnum.SOUTH_EAST,
            OrientationEnum.SOUTH_WEST,
            OrientationEnum.NORTH_WEST
    };

    private int size;

    public Cross(int size) {
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
        List<T> result = new ArrayList<>(ORIENTATIONS.length  * size + 1);
        result.add(target);

        for (OrientationEnum orientation : ORIENTATIONS){
            T last = target;
            for (int i = 0; i < size; ++i){
                last = Pathfinding.getCellByOrientation(mapWidth, mapHeight, cells, last, orientation);
                result.add(last);
            }
        }

        return result;
    }
}
