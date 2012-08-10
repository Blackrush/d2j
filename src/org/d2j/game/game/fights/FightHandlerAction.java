package org.d2j.game.game.fights;

/**
 * User: Blackrush
 * Date: 23/12/11
 * Time: 11:46
 * IDE : IntelliJ IDEA
 */
public interface FightHandlerAction {
    void call(IFightHandler obj) throws FightException;
}
