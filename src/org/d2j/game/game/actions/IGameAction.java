package org.d2j.game.game.actions;

/**
 * User: Blackrush
 * Date: 09/11/11
 * Time: 16:08
 * IDE:  IntelliJ IDEA
 */
public interface IGameAction {
    GameActionType getActionType();

    void begin() throws GameActionException;
    void end() throws GameActionException;
    void cancel() throws GameActionException;
}
