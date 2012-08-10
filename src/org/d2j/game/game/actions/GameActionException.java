package org.d2j.game.game.actions;

/**
 * User: Blackrush
 * Date: 13/11/11
 * Time: 16:04
 * IDE : IntelliJ IDEA
 */
public class GameActionException extends Exception {
    public GameActionException() {
    }

    public GameActionException(String message) {
        super(message);
    }

    public GameActionException(Exception e) {
        super(e.getMessage(), e.getCause());
    }
}
