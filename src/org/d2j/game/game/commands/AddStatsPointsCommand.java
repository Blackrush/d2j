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
 * Time: 19:19
 * To change this template use File | Settings | File Templates.
 */
public class AddStatsPointsCommand extends AbstractCommand {
    private final CharacterRepository characters;

    public AddStatsPointsCommand(CharacterRepository characters) {
        this.characters = characters;
    }

    @Override
    public String helpMessage() {
        return "Add stats points to a player.";
    }

    @Override
    public String[] usageParameters() {
        return new String[]{"STATS_POINTS", "ID or NAME of Character"};
    }

    @Override
    public String name() {
        return "stats_points";
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
            short statsPoints = Short.parseShort(args[0]);
            Character target = args.length > 1 ? characters.findByIdOrName(args[1]) : client.getCharacter();

            if (target == null){
                out.error("Unknown player {0}.", args[1]);
            }
            else{
                target.addStatsPoints(statsPoints);

                if (target.getOwner().isOnline()){
                    target.getOwner().getClient().getSession().write(target.getStatistics().getStatisticsMessage());
                }

                out.info("You successfully add {0} to {1}. He has {2} stats points now.",
                        statsPoints,
                        target.getName(),
                        target.getStatsPoints()
                );
            }
        }
    }
}
