package org.d2j.game.game.events;

import org.d2j.common.client.protocol.enums.ChannelEnum;

/**
 * User: Blackrush
 * Date: 22/12/11
 * Time: 16:58
 * IDE : IntelliJ IDEA
 */
public class MessageEvent implements IEvent {
    private long actorId;
    private String actorName;
    private ChannelEnum channel;
    private String message;

    @Override
    public EventType getEventType() {
        return EventType.MESSAGE;
    }

    public MessageEvent(long actorId, String actorName, ChannelEnum channel, String message) {
        this.actorId = actorId;
        this.actorName = actorName;
        this.channel = channel;
        this.message = message;
    }

    public long getActorId() {
        return actorId;
    }

    public void setActorId(long actorId) {
        this.actorId = actorId;
    }

    public String getActorName() {
        return actorName;
    }

    public void setActorName(String actorName) {
        this.actorName = actorName;
    }

    public ChannelEnum getChannel() {
        return channel;
    }

    public void setChannel(ChannelEnum channel) {
        this.channel = channel;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
