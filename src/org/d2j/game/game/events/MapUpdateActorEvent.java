package org.d2j.game.game.events;

import org.d2j.game.game.RolePlayActor;
import org.d2j.game.model.Character;

/**
 * User: Blackrush
 * Date: 22/12/11
 * Time: 17:00
 * IDE : IntelliJ IDEA
 */
public class MapUpdateActorEvent implements IEvent {
    public static enum MapUpdateType {
        ADD,
        REMOVE,
        UPDATE,
        UPDATE_ACCESSORIES,
    }

    private RolePlayActor actor;
    private MapUpdateType updateType;

    public MapUpdateActorEvent(RolePlayActor actor, MapUpdateType updateType) {
        this.actor = actor;
        this.updateType = updateType;
    }

    @Override
    public EventType getEventType() {
        return EventType.MAP_UPDATE_ACTOR;
    }

    public MapUpdateType getMapUpdateType(){
        return updateType;
    }

    public RolePlayActor getActor() {
        return actor;
    }
}
