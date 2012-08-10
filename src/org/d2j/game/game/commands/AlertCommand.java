package org.d2j.game.game.commands;

import org.d2j.common.Permissions;
import org.d2j.common.StringUtils;
import org.d2j.game.game.logging.DofusLogger;
import org.d2j.game.service.IWorld;
import org.d2j.game.service.game.GameClient;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 18/02/12
 * Time: 15:22
 * To change this template use File | Settings | File Templates.
 */
public class AlertCommand extends AbstractCommand {
    private final IWorld world;

    public AlertCommand(IWorld world) {
        this.world = world;
    }

    @Override
    public String helpMessage() {
        return "Show a message in a box for all online players.";
    }

    @Override
    public String[] usageParameters() {
        return new String[]{"MESSAGE"};
    }

    @Override
    public String name() {
        return "alert";
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
            String message = StringUtils.makeSentence(args);
            world.alertMessage(message);
        }
    }
}
