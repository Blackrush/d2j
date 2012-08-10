package org.d2j.game.game.commands;

import org.d2j.common.Permissions;
import org.d2j.game.game.logging.DofusLogger;
import org.d2j.game.model.*;
import org.d2j.game.model.Character;
import org.d2j.game.repository.CharacterRepository;
import org.d2j.game.repository.WaypointRepository;
import org.d2j.game.service.game.GameClient;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 22/02/12
 * Time: 21:08
 * To change this template use File | Settings | File Templates.
 */
public class AddWaypointsCommand extends AbstractCommand {
    private final CharacterRepository characters;
    private final WaypointRepository waypoints;

    public AddWaypointsCommand(CharacterRepository characters, WaypointRepository waypoints) {
        this.characters = characters;
        this.waypoints = waypoints;
    }

    @Override
    public String helpMessage() {
        return "Give all waypoints to the target.";
    }

    @Override
    public String[] usageParameters() {
        return new String[]{"ID or NAME of Character:facultative"};
    }

    @Override
    public String name() {
        return "waypoints";
    }

    @Override
    public Permissions level() {
        return Permissions.QUIZ_MASTER;
    }

    @Override
    public void parse(GameClient client, DofusLogger out, String[] args) {
        Character target = args.length > 0 ? characters.findByIdOrName(args[0]) : client.getCharacter();

        int num = 0;
        for (Waypoint waypoint : waypoints.all()){
            if (!target.getWaypoints().contains(waypoint)){
                target.getWaypoints().add(waypoint);
                ++num;
            }
        }

        if (target.getOwner().isOnline()){
            target.getOwner().getClient().getTchatOut().info("{0} gave you {1} waypoints.",
                    client.getCharacter().urlize(),
                    num
            );
        }

        out.info("{0} waypoints has been given to {1}.", num, target.urlize());
    }
}
