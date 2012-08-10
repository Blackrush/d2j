package org.d2j.game.game.commands;

import org.d2j.common.Permissions;
import org.d2j.game.game.logging.DofusLogger;
import org.d2j.game.service.game.GameClient;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 10/02/12
 * Time: 19:51
 * To change this template use File | Settings | File Templates.
 */
public class EditSizeCommand extends AbstractCommand {
    @Override
    public String helpMessage() {
        return "Edit your size. Empty for recover initial size.";
    }

    @Override
    public String[] usageParameters() {
        return new String[]{"SIZE:facultative"};
    }

    @Override
    public String name() {
        return "size";
    }

    @Override
    public Permissions level() {
        return Permissions.QUIZ_MASTER;
    }

    @Override
    public void parse(GameClient client, DofusLogger out, String[] args) {
        short size = args.length > 0 ? Short.parseShort(args[0]) : 100;
        client.getCharacter().setSize(size);
        client.getCharacter().getCurrentMap().updateActor(client.getCharacter());
    }
}
