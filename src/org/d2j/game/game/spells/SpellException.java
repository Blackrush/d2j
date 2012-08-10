package org.d2j.game.game.spells;

import org.d2j.game.game.fights.FightException;

/**
 * User: Blackrush
 * Date: 09/12/11
 * Time: 14:50
 * IDE : IntelliJ IDEA
 */
public class SpellException extends FightException {
    public SpellException() {

    }

    public SpellException(String message) {
        super(message);
    }
}
