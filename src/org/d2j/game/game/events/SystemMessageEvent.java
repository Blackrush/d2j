package org.d2j.game.game.events;

/**
 * User: Blackrush
 * Date: 24/12/11
 * Time: 19:11
 * IDE : IntelliJ IDEA
 */
public class SystemMessageEvent implements IEvent {
    private String message;

    public SystemMessageEvent(String message) {
        this.message = message;
    }

    @Override
    public EventType getEventType() {
        return EventType.SYSTEM_MESSAGE;
    }

    public String getMessage() {
        return message;
    }
}
