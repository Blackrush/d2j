package org.d2j.game.game.commands;

import org.d2j.common.Permissions;
import org.d2j.common.StringUtils;
import org.d2j.game.game.logging.DofusLogger;
import org.d2j.game.model.*;
import org.d2j.game.model.Character;
import org.d2j.game.service.game.GameClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 21/01/12
 * Time: 12:42
 * To change this template use File | Settings | File Templates.
 */
public class MapAnnounceCommand extends AbstractCommand {
    @Override
    public String helpMessage() {
        return "Show a message for all players on your map.";
    }

    @Override
    public String[] usageParameters() {
        return new String[]{"MESSAGE"};
    }

    @Override
    public String name() {
        return "mapannounce";
    }

    @Override
    public Permissions level() {
        return Permissions.QUIZ_MASTER;
    }

    @Override
    public void parse(GameClient client, DofusLogger out, String[] args) {
        if (args.length < 1){
            out.error("Usage: " + usage());
        }
        else{
            client.getCharacter().getCurrentMap().announce(StringUtils.makeSentence(args));
        }
    }
}
