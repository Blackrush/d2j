package org.d2j.game.game.actions;

import org.d2j.game.game.fights.FightException;

/**
 * User: Blackrush
 * Date: 14/11/11
 * Time: 19:31
 * IDE : IntelliJ IDEA
 */
public interface IInvitation extends IGameAction {
    void accept() throws GameActionException;
    void decline() throws GameActionException;
}
