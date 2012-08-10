package org.d2j.game.game.pathfinding;

/**
 * Created by IntelliJ IDEA.
 * User: Blackrush
 * Date: 06/03/12
 * Time: 11:17
 */
public class NotWalkableException extends PathfindingException {
    public NotWalkableException() {
    }

    public NotWalkableException(String message) {
        super(message);
    }
}
