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
 * Time: 19:09
 * To change this template use File | Settings | File Templates.
 */
public class EditLifeCommand extends AbstractCommand {
    private final CharacterRepository characters;

    public EditLifeCommand(CharacterRepository characters) {
        this.characters = characters;
    }

    @Override
    public String helpMessage() {
        return "Add or substract life of a player.";
    }

    @Override
    public String[] usageParameters() {
        return new String[]{"PERCENT", "ID or NAME of Character"};
    }

    @Override
    public String name() {
        return "pdvper";
    }

    @Override
    public Permissions level() {
        return Permissions.QUIZ_MASTER;
    }

    @Override
    public void parse(GameClient client, DofusLogger out, String[] args) {
        if (args.length < 1 || args.length > 2){
            out.error("Usage: " + usage());
        }
        else{
            int percent = Integer.parseInt(args[0]);
            Character target = args.length > 1 ? characters.findByIdOrName(args[1]) : client.getCharacter();

            if (target == null){
                out.error("Unknown player {0}.", args[1]);
            }
            else if (target.getOwner().isOnline() && target.getOwner().getClient().isBusy()){
                out.error("{0} is offline or busy.", target.getName());
            }
            else{
                target.getStatistics().setLifeByPercent(percent);
                target.getOwner().getClient().getSession().write(target.getStatistics().getStatisticsMessage());

                out.info("{0} has now {1} life points of {2}.", target.getName(), target.getStatistics().getLife(), target.getStatistics().getMaxLife());
            }
        }
    }
}
