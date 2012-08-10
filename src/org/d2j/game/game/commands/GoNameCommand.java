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
 * Time: 12:37
 * To change this template use File | Settings | File Templates.
 */
public class GoNameCommand extends AbstractCommand {
    private final CharacterRepository characters;

    public GoNameCommand(CharacterRepository characters) {
        this.characters = characters;
    }

    @Override
    public String helpMessage() {
        return "Teleport your character to the target.";
    }

    @Override
    public String[] usageParameters() {
        return new String[]{"ID or NAME of Character"};
    }

    @Override
    public String name() {
        return "goname";
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
                out.error("Unknown target {0}.", args[0]);
            }
            else if (!target.getOwner().isOnline()){
                out.error("{0} isn't online.", target.getName());
            }
            else if (target.getOwner().getClient().isBusy()){
                out.error("{0} is busy.", target.getName());
            }
            else{
                try {
                    RolePlayMovement.teleport(
                            client,
                            target.getCurrentMap(),
                            target.getCurrentCell()
                    );

                    out.info("You've successfully teleported to {0}.",
                            target.getOwner().isOnline() ?
                            urlize(target.getName()) : target.getName()
                    );
                } catch (GameActionException e) {
                    out.error("Can't teleport to \"{0}\" because : {1}", target.getName(), e.getMessage());
                }
            }
        }
    }
}
