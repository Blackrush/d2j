package org.d2j.game.model;

import org.d2j.utils.database.entity.IBaseEntity;

/**
 * User: Blackrush
 * Date: 12/11/11
 * Time: 17:13
 * IDE : IntelliJ IDEA
 */
public class MapTrigger implements IBaseEntity<Integer> {
    private Integer id;
    private Map map;
    private short cellId;
    private Map nextMap;
    private short nextCellId;

    public MapTrigger() {

    }

    public MapTrigger(Integer id, Map map, short cellId, Map nextMap, short nextCellId) {
        this.id = id;
        this.map = map;
        this.cellId = cellId;
        this.nextMap = nextMap;
        this.nextCellId = nextCellId;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public Map getMap() {
        return map;
    }

    public short getCellId() {
        return cellId;
    }

    public Map getNextMap() {
        return nextMap;
    }

    public short getNextCellId() {
        return nextCellId;
    }
}
