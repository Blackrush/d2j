package org.d2j.game.game.commands;

import org.d2j.common.Permissions;
import org.d2j.common.client.protocol.enums.WorldStateEnum;
import org.d2j.game.game.logging.DofusLogger;
import org.d2j.game.service.IWorld;
import org.d2j.game.service.game.GameClient;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 25/01/12
 * Time: 18:50
 * To change this template use File | Settings | File Templates.
 */
public class StateCommand extends AbstractCommand {
    private final IWorld world;

    public StateCommand(IWorld world) {
        this.world = world;
    }

    @Override
    public String helpMessage() {
        return "Update game server's state.";
    }

    @Override
    public String[] usageParameters() {
        return new String[]{"STATE:facultative"};
    }

    @Override
    public String name() {
        return "state";
    }

    @Override
    public Permissions level() {
        return Permissions.ADMINISTRATOR;
    }

    @Override
    public void parse(GameClient client, DofusLogger out, String[] args) {
        if (args.length > 0) {
            WorldStateEnum state = WorldStateEnum.valueOf(Integer.parseInt(args[0]));
            world.setState(state);
        }

        out.info("Game server is now {0}.", world.getState().toString());
    }
}
