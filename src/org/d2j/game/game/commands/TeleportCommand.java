package org.d2j.game.game.commands;

import org.d2j.common.Permissions;
import org.d2j.game.game.actions.GameActionException;
import org.d2j.game.game.actions.RolePlayMovement;
import org.d2j.game.game.logging.DofusLogger;
import org.d2j.game.model.*;
import org.d2j.game.model.Character;
import org.d2j.game.repository.CharacterRepository;
import org.d2j.game.service.game.GameClient;
import org.d2j.utils.database.repository.IBaseEntityRepository;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 08/02/12
 * Time: 19:01
 * To change this template use File | Settings | File Templates.
 */
public class TeleportCommand extends AbstractCommand {
    private final IBaseEntityRepository<Map, Integer> maps;
    private final CharacterRepository characters;

    public TeleportCommand(IBaseEntityRepository<Map, Integer> maps, CharacterRepository characters) {
        this.maps = maps;
        this.characters = characters;
    }

    @Override
    public String helpMessage() {
        return "Teleport a player to the specified target.";
    }

    @Override
    public String[] usageParameters() {
        return new String[]{"ID of Map", "CELL_ID", "ID or NAME of Character:facultative"};
    }

    @Override
    public String name() {
        return "teleport";
    }

    @Override
    public Permissions level() {
        return Permissions.QUIZ_MASTER;
    }

    @Override
    public void parse(GameClient client, DofusLogger out, String[] args) {
        if (args.length < 1 || args.length > 3){
            out.error("Usage: " + usage());
        }
        else{
            Map targetMap = maps.findById(Integer.parseInt(args[0]));
            Short targetCell = Short.parseShort(args[1]);
            Character target = args.length > 2 ? characters.findByIdOrName(args[2]) : client.getCharacter();

            if (targetMap == null){
                out.error("Unknown map {0}.", args[0]);
            }
            else if (target == null){
                out.error("Unknown player {0}.", args[2]);
            }
            else if (!target.getOwner().isOnline()){
                out.error("{0} is offline.", target.getName());
            }
            else if (!target.getOwner().getClient().isBusy()){
                out.error("{0} is busy.", target.getName());
            }
            else{
                try {
                    RolePlayMovement.teleport(
                            target.getOwner().getClient(),
                            targetMap,
                            targetCell
                    );
                } catch (GameActionException e) {
                    out.error("You can't teleport this player because : " + e.getMessage());
                }
            }
        }
    }
}
