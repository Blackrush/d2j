package org.d2j.game.model;

import org.d2j.utils.database.entity.IBaseEntity;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 22/02/12
 * Time: 19:10
 * To change this template use File | Settings | File Templates.
 */
public class Waypoint implements IBaseEntity<Short> {
    public static long getCost(Map m1, Map m2){
        return 10 * (Math.abs(m1.getAbscissa() - m2.getAbscissa()) + Math.abs(m1.getOrdinate() - m2.getOrdinate()) - 1);
    }

    private short id;
    private Map map;
    private short cellId;

    public Waypoint(short id, Map map, short cellId) {
        this.id = id;
        this.map = map;
        this.cellId = cellId;
    }

    @Override
    public Short getId() {
        return id;
    }

    public Map getMap() {
        return map;
    }

    public short getCellId() {
        return cellId;
    }

    public long getCost(Map target){
        return getCost(map, target);
    }
}
