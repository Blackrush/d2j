package org.d2j.game.game.logging;

import org.apache.mina.core.session.IoSession;
import org.d2j.common.StringUtils;
import org.d2j.common.client.protocol.ChannelGameMessageFormatter;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 02/02/12
 * Time: 17:33
 * To change this template use File | Settings | File Templates.
 */
public class TchatLogger implements DofusLogger {
    private final IoSession session;

    public TchatLogger(IoSession session) {
        this.session = session;
    }

    @Override
    public void info(String message) {
        session.write(ChannelGameMessageFormatter.informationMessage("<font color=\"#009900\">(<b>Information</b>) " + message + "</font>"));
    }

    @Override
    public void info(String message, Object... parameters) {
        session.write(ChannelGameMessageFormatter.informationMessage("<font color=\"#009900\">(<b>Information</b>) " + StringUtils.formatString(message, parameters) + "</font>"));
    }

    @Override
    public void log(String message) {
        session.write(ChannelGameMessageFormatter.informationMessage(message));
    }

    @Override
    public void log(String message, Object... parameters) {
        session.write(ChannelGameMessageFormatter.informationMessage(StringUtils.formatString(message, parameters)));
    }

    @Override
    public void error(String message) {
        session.write(ChannelGameMessageFormatter.informationMessage("<font color=\"#C10000\">(<b>Erreur</b>) " + message + "</font>"));
    }

    @Override
    public void error(String message, Object... parameters) {
        session.write(ChannelGameMessageFormatter.informationMessage("<font color=\"#C10000\">(<b>Erreur</b>) " + StringUtils.formatString(message, parameters) + "</font>"));
    }
}
