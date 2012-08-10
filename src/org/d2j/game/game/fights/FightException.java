package org.d2j.game.game.fights;

import org.d2j.game.game.actions.GameActionException;

/**
 * User: Blackrush
 * Date: 16/11/11
 * Time: 19:18
 * IDE : IntelliJ IDEA
 */
public class FightException extends GameActionException {
    public FightException() {
    }

    public FightException(String message) {
        super(message);
    }

    public FightException(Exception e) {
        super(e);
    }
}
