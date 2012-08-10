package org.d2j.game.game.events;

import org.d2j.game.model.Character;

/**
 * User: Blackrush
 * Date: 22/12/11
 * Time: 21:34
 * IDE : IntelliJ IDEA
 */
public class PartyUpdateMemberEvent implements IEvent {
    public static enum PartyUpdateMemberType {
        ADD,
        REMOVE,
        REFRESH,
        LEADER;
    }

    private Character member;
    private PartyUpdateMemberType partyUpdateMemberType;

    public PartyUpdateMemberEvent(Character member, PartyUpdateMemberType partyUpdateMemberType) {
        this.member = member;
        this.partyUpdateMemberType = partyUpdateMemberType;
    }

    @Override
    public EventType getEventType() {
        return EventType.PARTY_UPDATE_ACTOR;
    }

    public PartyUpdateMemberType getPartyUpdateMemberType(){
        return partyUpdateMemberType;
    }

    public Character getMember() {
        return member;
    }
}
