package org.d2j.game.game.commands;

import org.d2j.common.Permissions;
import org.d2j.game.game.logging.DofusLogger;
import org.d2j.game.service.game.GameClient;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 08/01/12
 * Time: 15:58
 * To change this template use File | Settings | File Templates.
 */
public interface ICommand {
    String name();
    String help();
    Permissions level();

    void parse(GameClient client, DofusLogger out, String[] args);
}
