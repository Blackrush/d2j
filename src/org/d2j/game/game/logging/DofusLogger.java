package org.d2j.game.game.logging;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 02/02/12
 * Time: 17:31
 * To change this template use File | Settings | File Templates.
 */
public interface DofusLogger {
    void info(String message);
    void info(String message, Object... parameters);

    void log(String message);
    void log(String message, Object... parameters);

    void error(String message);
    void error(String message, Object... parameters);
}
