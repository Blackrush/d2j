package org.d2j.game.game.commands;

import org.d2j.common.Permissions;
import org.d2j.game.game.actions.GameActionException;
import org.d2j.game.game.actions.RolePlayMovement;
import org.d2j.game.game.logging.DofusLogger;
import org.d2j.game.model.Character;
import org.d2j.game.repository.CharacterRepository;
import org.d2j.game.service.game.GameClient;

import static org.d2j.common.client.protocol.InfoGameMessageFormatter.urlize;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 21/01/12
 * Time: 12:27
 * To change this template use File | Settings | File Templates.
 */
public class NameGoCommand extends AbstractCommand {
    private final CharacterRepository characters;

    public NameGoCommand(CharacterRepository characters) {
        this.characters = characters;
    }

    @Override
    public String helpMessage() {
        return "Teleport a player to you.";
    }

    @Override
    public String[] usageParameters() {
        return new String[]{"ID or NAME of Character"};
    }

    @Override
    public String name() {
        return "namego";
    }

    @Override
    public Permissions level() {
        return Permissions.QUIZ_MASTER;
    }

    @Override
    public void parse(GameClient client, DofusLogger out, String[] args) {
        if (args.length != 1){
            out.error("Usage: " + usage());
        }
        else{
            Character target = characters.findByIdOrName(args[0]);

            if (target == null){
                out.error("Unknown target " + args + ".");
            }
            else if (!target.getOwner().isOnline()){
                out.error("Target \"{0}\" isn't connected.", target.getName());
            }
            else if (target.getOwner().getClient().isBusy()){
                out.error("Target \"{0}\" is busy.", urlize(target.getName()));
            }
            else{
                try {
                    RolePlayMovement.teleport(
                            target.getOwner().getClient(),
                            client.getCharacter().getCurrentMap(),
                            client.getCharacter().getCurrentCell()
                    );

                    target.getOwner().getClient().getTchatOut().info("You've been teleported by {0}.",
                            urlize(client.getCharacter().getName())
                    );
                    out.info("You've successfully teleported {0}.",
                            urlize(client.getCharacter().getName())
                    );
                } catch (GameActionException e) {
                    out.error("Can't teleport \"{0}\" because : {1}", target.getName(), e.getMessage());
                }
            }
        }
    }
}
