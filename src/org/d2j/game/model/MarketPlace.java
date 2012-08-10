package org.d2j.game.model;

import org.d2j.utils.database.entity.IBaseEntity;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 27/02/12
 * Time: 10:03
 * To change this template use File | Settings | File Templates.
 */
public class MarketPlace implements IBaseEntity<Short> {
    private short id;
    private Npc npc;

    public MarketPlace(short id, Npc npc) {
        this.id = id;
        this.npc = npc;
    }

    @Override
    public Short getId() {
        return id;
    }

    public Npc getNpc() {
        return npc;
    }

    public Map getMap() {
        return npc.getMap();
    }
}
