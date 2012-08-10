package org.d2j.game.game.events;

import org.d2j.game.model.Map;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 19/02/12
 * Time: 11:14
 * To change this template use File | Settings | File Templates.
 */
public class FightsUpdateEvent implements IEvent {
    private Map map;

    public FightsUpdateEvent(Map map) {
        this.map = map;
    }

    @Override
    public EventType getEventType() {
        return EventType.FIGHTS_UPDATE;
    }

    public Map getMap() {
        return map;
    }
}
