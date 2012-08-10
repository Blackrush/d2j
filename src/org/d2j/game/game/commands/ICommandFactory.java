package org.d2j.game.game.commands;

import org.d2j.game.service.game.GameClient;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 08/01/12
 * Time: 16:32
 * To change this template use File | Settings | File Templates.
 */
public interface ICommandFactory {
    int init();
    void parse(GameClient client, String command, boolean console);
}
