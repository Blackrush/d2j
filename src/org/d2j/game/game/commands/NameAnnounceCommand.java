package org.d2j.game.game.commands;

import org.d2j.common.Permissions;
import org.d2j.common.StringUtils;
import org.d2j.common.client.protocol.InfoGameMessageFormatter;
import org.d2j.game.game.events.SystemMessageEvent;
import org.d2j.game.game.logging.DofusLogger;
import org.d2j.game.service.IWorld;
import org.d2j.game.service.game.GameClient;

import static org.d2j.common.StringUtils.format;
import static org.d2j.common.StringUtils.makeSentence;
import static org.d2j.common.client.protocol.InfoGameMessageFormatter.urlize;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 21/01/12
 * Time: 12:53
 * To change this template use File | Settings | File Templates.
 */
public class NameAnnounceCommand extends AbstractCommand {
    public static final String PATTERN = "(Annonce) {0} : {1}";

    private final IWorld world;

    public NameAnnounceCommand(IWorld world) {
        this.world = world;
    }

    @Override
    public String helpMessage() {
        return "Show a message with your name for each connected player.";
    }

    @Override
    public String[] usageParameters() {
        return new String[]{"MESSAGE"};
    }

    @Override
    public String name() {
        return "nameannounce";
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
            world.systemMessage(new SystemMessageEvent(format(
                    PATTERN,
                    urlize(client.getCharacter().getName()),
                    makeSentence(args)
            )));
        }
    }
}
