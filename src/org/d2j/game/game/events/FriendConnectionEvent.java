package org.d2j.game.game.events;

import org.d2j.game.model.GameAccount;

/**
 * Created by IntelliJ IDEA.
 * User: blackrush
 * Date: 31/12/11
 * Time: 14:53
 * To change this template use File | Settings | File Templates.
 */
public class FriendConnectionEvent implements IEvent {
    private GameAccount friend;

    public FriendConnectionEvent(GameAccount friend) {
        this.friend = friend;
    }

    @Override
    public EventType getEventType() {
        return EventType.FRIEND_CONNECTION;
    }

    public GameAccount getFriend() {
        return friend;
    }

    public void setFriend(GameAccount friend) {
        this.friend = friend;
    }
}
