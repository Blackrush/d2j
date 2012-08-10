package org.d2j.game.game.events;

import org.d2j.game.model.Character;

/**
 * User: Blackrush
 * Date: 27/12/11
 * Time: 15:17
 * IDE : IntelliJ IDEA
 */
public class LevelUpEvent implements IEvent {
    private Character character;

    public LevelUpEvent(Character character) {
        this.character = character;
    }

    @Override
    public EventType getEventType() {
        return EventType.LEVEL_UP;
    }

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }
}
