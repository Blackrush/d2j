package org.d2j.game.game.events;

import org.d2j.common.client.protocol.enums.InfoTypeEnum;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 29/01/12
 * Time: 10:29
 * To change this template use File | Settings | File Templates.
 */
public class InfoMessageEvent implements IEvent {
    private InfoTypeEnum type;

    public InfoMessageEvent(InfoTypeEnum type) {
        this.type = type;
    }

    @Override
    public EventType getEventType() {
        return EventType.INFO_MESSAGE;
    }

    public InfoTypeEnum getInfoType(){
        return type;
    }
}
