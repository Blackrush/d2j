package org.d2j.game.game.events;

import org.d2j.game.game.RolePlayActor;
import org.d2j.game.game.fights.IFighter;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 19/02/12
 * Time: 11:19
 * To change this template use File | Settings | File Templates.
 */
public class FlagMembersUpdateEvent implements IEvent {
    public static enum UpdateType {
        ADD,
        REMOVE,
    }

    private Collection<IFighter> members;

    public FlagMembersUpdateEvent(Collection<IFighter> members) {
        this.members = members;
    }

    @Override
    public EventType getEventType() {
        return EventType.FLAG_MEMBERS_UPDATE;
    }

    public Collection<IFighter> getMembers() {
        return members;
    }
}
