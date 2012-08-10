package org.d2j.game.game.logging;

import org.apache.mina.core.session.IoSession;
import org.d2j.common.StringUtils;
import org.d2j.common.client.protocol.BasicGameMessageFormatter;

import static org.d2j.common.StringUtils.format;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 02/02/12
 * Time: 17:37
 * To change this template use File | Settings | File Templates.
 */
public class ConsoleLogger implements DofusLogger { //TODO
    private final IoSession session;

    public ConsoleLogger(IoSession session) {
        this.session = session;
    }

    @Override
    public void info(String message) {
        session.write(BasicGameMessageFormatter.consoleMessage(message));
    }

    @Override
    public void info(String message, Object... parameters) {
        session.write(BasicGameMessageFormatter.consoleMessage(format(message, parameters)));
    }

    @Override
    public void log(String message) {
        session.write(BasicGameMessageFormatter.consoleMessage(message, 1));
    }

    @Override
    public void log(String message, Object... parameters) {
        session.write(BasicGameMessageFormatter.consoleMessage(format(message, parameters), 1));
    }

    @Override
    public void error(String message) {
        session.write(BasicGameMessageFormatter.consoleMessage(message, 0));
    }

    @Override
    public void error(String message, Object... parameters) {
        session.write(BasicGameMessageFormatter.consoleMessage(format(message, parameters), 0));
    }
}
