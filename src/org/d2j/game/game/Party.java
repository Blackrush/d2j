package org.d2j.game.game;

import org.d2j.common.client.protocol.enums.ChannelEnum;
import org.d2j.game.game.events.MessageEvent;
import org.d2j.game.game.events.PartyUpdateMemberEvent;
import org.d2j.game.model.Character;

import java.util.Collection;
import java.util.HashMap;
import java.util.Observable;

import static org.d2j.common.CollectionUtils.first;
import static org.d2j.common.CollectionUtils.last;

/**
 * User: Blackrush
 * Date: 22/12/11
 * Time: 19:25
 * IDE : IntelliJ IDEA
 */
public class Party extends Observable {
    private Character leader;
    private HashMap<Long, Character> members;

    public Party(Character leader) {
        this.leader = leader;

        this.members = new HashMap<>();
        this.members.put(this.leader.getId(), this.leader);
    }

    public Collection<Character> getMembers() {
        return members.values();
    }

    public Character getLeader() {
        return leader;
    }

    public void setLeader(Character leader) {
        if (members.containsKey(leader.getId())){
            this.leader = leader;

            setChanged();
            notifyObservers(new PartyUpdateMemberEvent(leader, PartyUpdateMemberEvent.PartyUpdateMemberType.LEADER));
        }
    }

    public void addMember(Character member){
        if (!members.containsKey(member.getId())){
            members.put(member.getId(), member);

            setChanged();
            notifyObservers(new PartyUpdateMemberEvent(member, PartyUpdateMemberEvent.PartyUpdateMemberType.ADD));
        }
    }

    public boolean removeMember(Character member){
        if (members.containsKey(member.getId())){
            members.remove(member.getId());

            setChanged();
            notifyObservers(new PartyUpdateMemberEvent(member, PartyUpdateMemberEvent.PartyUpdateMemberType.REMOVE));

            if (members.size() <= 1){
                Character last = last(members.values());

                setChanged();
                notifyObservers(new PartyUpdateMemberEvent(last, PartyUpdateMemberEvent.PartyUpdateMemberType.REMOVE));
            }
            else if (leader == member){
                setLeader(first(members.values()));
            }

            return true;
        }
        return false;
    }

    public void speak(Character member, String message){
        if (members.containsKey(member.getId())){
            setChanged();
            notifyObservers(new MessageEvent(
                    member.getId(),
                    member.getName(),
                    ChannelEnum.Party,
                    message
            ));
        }
    }

    public void refresh(Character member){
        if (members.containsKey(member.getId())){
            setChanged();
            notifyObservers(new PartyUpdateMemberEvent(member, PartyUpdateMemberEvent.PartyUpdateMemberType.REFRESH));
        }
    }

    public Character getMember(long memberId) {
        return members.get(memberId);
    }

    public boolean containsMember(long memberId) {
        return members.containsKey(memberId);
    }
}
