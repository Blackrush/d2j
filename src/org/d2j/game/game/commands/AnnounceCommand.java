package org.d2j.game.game.commands;

import org.d2j.common.Permissions;
import org.d2j.common.StringUtils;
import org.d2j.game.game.events.SystemMessageEvent;
import org.d2j.game.game.logging.DofusLogger;
import org.d2j.game.service.IWorld;
import org.d2j.game.service.game.GameClient;

import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 08/01/12
 * Time: 16:14
 * To change this template use File | Settings | File Templates.
 */
public class AnnounceCommand extends AbstractCommand {
    public static final String PATTERN = "<font color=\"#0010FF\">(<b>Annonce</b>) {0}</font>";

    private IWorld world;

    public AnnounceCommand(IWorld world) {
        this.world = world;
    }

    @Override
    public String helpMessage() {
        return "Show a message for each connected player.";
    }

    @Override
    public String[] usageParameters() {
        return new String[]{
                "MESSAGE"
        };
    }

    @Override
    public String name() {
        return "announce";
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
            world.systemMessage(new SystemMessageEvent(StringUtils.format(PATTERN, StringUtils.makeSentence(args))));
        }
    }
}
