package org.d2j.game.model;

import org.d2j.utils.database.entity.IEntity;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 22/02/12
 * Time: 19:19
 * To change this template use File | Settings | File Templates.
 */
public class WaypointRecord implements IEntity<Long> {
    private long id;
    private Character character;
    private Waypoint waypoint;

    public WaypointRecord(Character character, Waypoint waypoint) {
        this.character = character;
        this.waypoint = waypoint;
    }

    public WaypointRecord(long id, Character character, Waypoint waypoint) {
        this.id = id;
        this.character = character;
        this.waypoint = waypoint;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Character getCharacter() {
        return character;
    }

    public Waypoint getWaypoint() {
        return waypoint;
    }

    @Override
    public void beforeSave() {
    }

    @Override
    public void onSaved() {
    }
}
