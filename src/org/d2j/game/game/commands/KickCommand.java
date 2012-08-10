package org.d2j.game.game.commands;

import org.d2j.common.Permissions;
import org.d2j.common.StringUtils;
import org.d2j.common.client.protocol.InfoGameMessageFormatter;
import org.d2j.game.game.events.SystemMessageEvent;
import org.d2j.game.game.logging.DofusLogger;
import org.d2j.game.model.*;
import org.d2j.game.model.Character;
import org.d2j.game.repository.CharacterRepository;
import org.d2j.game.service.IWorld;
import org.d2j.game.service.game.GameClient;

import java.util.Arrays;

import static org.d2j.common.client.protocol.InfoGameMessageFormatter.urlize;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 09/01/12
 * Time: 19:36
 * To change this template use File | Settings | File Templates.
 */
public class KickCommand extends AbstractCommand {
    private IWorld world;
    private CharacterRepository characters;

    public KickCommand(IWorld world, CharacterRepository characters) {
        this.world = world;
        this.characters = characters;
    }

    @Override
    public String name() {
        return "kick";
    }

    @Override
    public String helpMessage() {
        return "Close the target's connection.";
    }

    @Override
    public String[] usageParameters() {
        return new String[]{
                "ID or NAME of Character",
                "ANONYMOUSLY",
                "REASON",
        };
    }

    @Override
    public Permissions level() {
        return Permissions.GAME_MASTER;
    }

    @Override
    public void parse(GameClient client, DofusLogger out, String[] args) {
        if (args.length < 1){
            out.error("Usage: " + usage());
        }
        else {
            boolean anonymously = args[1].equals("1");

            String reason = args.length > 2 ?
                    StringUtils.join(Arrays.copyOfRange(args, 2, args.length), " ") :
                    "no reason";

            Character target = characters.findByIdOrName(args[0]);

            if (target == null){
                out.error("Unknown target {0}.", args[0]);
            }
            else if (target == client.getCharacter()){
                out.error("You can not kick yourself.");
            }
            else if (!target.getOwner().isOnline()){
                out.error("{0} isn't connected.", target.getName());
            }
            else{
                if (anonymously){
                    target.getOwner().getClient().kick(reason);
                }
                else{
                    target.getOwner().getClient().kick(client.getCharacter(), reason);

                    world.systemMessage(new SystemMessageEvent(StringUtils.format(
                            "{0} has been kicked by {1} because : <i>{2}</i>",
                            target.getName(),
                            urlize(client.getCharacter().getName()),
                            reason
                    )));
                }
            }
        }
    }
}
