package org.d2j.game.game.events;

import org.d2j.common.client.protocol.enums.FightAttributeType;
import org.d2j.game.game.fights.IFighter;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 19/02/12
 * Time: 15:21
 * To change this template use File | Settings | File Templates.
 */
public class FlagAttributeUpdateEvent implements IEvent {
    private IFighter leader;
    private FightAttributeType attribute;
    private boolean active;

    public FlagAttributeUpdateEvent(IFighter leader, FightAttributeType attribute, boolean active) {
        this.leader = leader;
        this.attribute = attribute;
        this.active = active;
    }

    @Override
    public EventType getEventType() {
        return EventType.FLAG_ATTRIBUTE_UPDATE;
    }

    public IFighter getLeader() {
        return leader;
    }

    public FightAttributeType getAttribute() {
        return attribute;
    }

    public boolean isActive() {
        return active;
    }
}
