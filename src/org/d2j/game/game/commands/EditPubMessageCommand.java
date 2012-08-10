package org.d2j.game.game.commands;

import org.d2j.common.Permissions;
import org.d2j.common.StringUtils;
import org.d2j.game.game.logging.DofusLogger;
import org.d2j.game.service.game.GameClient;
import org.d2j.game.service.game.GameService;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 05/02/12
 * Time: 20:21
 * To change this template use File | Settings | File Templates.
 */
public class EditPubMessageCommand extends AbstractCommand {
    private final GameService gameService;

    public EditPubMessageCommand(GameService gameService) {
        this.gameService = gameService;
    }

    @Override
    public String helpMessage() {
        return "Edit message pub.";
    }

    @Override
    public String[] usageParameters() {
        return new String[]{"ORDER", "MESSAGE"};
    }

    @Override
    public String name() {
        return "pub_edit";
    }

    @Override
    public Permissions level() {
        return Permissions.ADMINISTRATOR;
    }

    @Override
    public void parse(GameClient client, DofusLogger out, String[] args) {
        if (args.length < 2){
            out.error("Usage: " + usage());
        }
        else{
            int index = Integer.parseInt(args[0]);
            String message = StringUtils.makeSentence(args, 1);

            gameService.getPubSystem().setMessage(index, message);
        }
    }
}
