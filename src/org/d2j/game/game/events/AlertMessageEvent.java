package org.d2j.game.game.events;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 18/02/12
 * Time: 15:24
 * To change this template use File | Settings | File Templates.
 */
public class AlertMessageEvent implements IEvent {
    private String message;

    @Override
    public EventType getEventType() {
        return EventType.ALERT_MESSAGE;
    }

    public AlertMessageEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
