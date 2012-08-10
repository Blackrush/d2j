package org.d2j.game.game.events;

import org.d2j.game.game.fights.Fight;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 18/02/12
 * Time: 09:54
 * To change this template use File | Settings | File Templates.
 */
public class FlagUpdateEvent implements IEvent {
    public static enum UpdateType {
        ADD,
        REMOVE,
    }

    private Fight fight;
    private UpdateType type;

    public FlagUpdateEvent(Fight fight, UpdateType type) {
        this.fight = fight;
        this.type = type;
    }

    @Override
    public EventType getEventType() {
        return EventType.FLAG_UPDATE;
    }

    public Fight getFight() {
        return fight;
    }

    public UpdateType getUpdateType() {
        return type;
    }
}
