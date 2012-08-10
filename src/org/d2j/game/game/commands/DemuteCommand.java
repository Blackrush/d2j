package org.d2j.game.game.commands;

import org.d2j.common.Permissions;
import org.d2j.game.game.logging.DofusLogger;
import org.d2j.game.model.*;
import org.d2j.game.model.Character;
import org.d2j.game.repository.CharacterRepository;
import org.d2j.game.service.game.GameClient;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 08/02/12
 * Time: 18:29
 * To change this template use File | Settings | File Templates.
 */
public class DemuteCommand extends AbstractCommand {
    private final CharacterRepository characters;

    public DemuteCommand(CharacterRepository characters) {
        this.characters = characters;
    }

    @Override
    public String helpMessage() {
        return "Demute a player.";
    }

    @Override
    public String[] usageParameters() {
        return new String[]{"ID or NAME of Character"};
    }

    @Override
    public String name() {
        return "demute";
    }

    @Override
    public Permissions level() {
        return Permissions.GAME_MASTER;
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
            else if (!target.getOwner().isMuted()){
                out.error("Target {0} is already unmuted.", target.getName());
            }
            else{
                target.getOwner().setMuted(false);
                if (target.getOwner().isOnline()){
                    target.getOwner().getClient().getTchatOut().info("You have been unmuted by {0}.", client.getCharacter().getName());
                }
                out.info("{0} is successfully unmuted.", target.getName());
            }
        }
    }
}
