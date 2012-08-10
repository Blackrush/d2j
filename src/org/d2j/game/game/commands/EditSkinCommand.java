package org.d2j.game.game.commands;

import org.d2j.common.Permissions;
import org.d2j.game.game.logging.DofusLogger;
import org.d2j.game.model.BreedTemplate;
import org.d2j.game.service.game.GameClient;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 10/02/12
 * Time: 19:47
 * To change this template use File | Settings | File Templates.
 */
public class EditSkinCommand extends AbstractCommand {
    @Override
    public String helpMessage() {
        return "Edit your skin. Empty for recover initial skin.";
    }

    @Override
    public String[] usageParameters() {
        return new String[]{"SKIN_ID:facultative"};
    }

    @Override
    public String name() {
        return "skin";
    }

    @Override
    public Permissions level() {
        return Permissions.QUIZ_MASTER;
    }

    @Override
    public void parse(GameClient client, DofusLogger out, String[] args) {
        short skin = args.length > 0 ? Short.parseShort(args[0]) :
                                       client.getCharacter().getBreed().getSkin(client.getCharacter().getGender());

        client.getCharacter().setSkin(skin);

        client.getCharacter().getCurrentMap().updateActor(client.getCharacter());
    }
}
