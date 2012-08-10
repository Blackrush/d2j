package org.d2j.game.game.commands;

import org.d2j.common.Permissions;
import org.d2j.game.configuration.IGameConfiguration;
import org.d2j.game.game.actions.GameActionException;
import org.d2j.game.game.actions.RolePlayMovement;
import org.d2j.game.game.logging.DofusLogger;
import org.d2j.game.model.Map;
import org.d2j.game.service.game.GameClient;
import org.d2j.utils.database.repository.IBaseEntityRepository;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 24/01/12
 * Time: 20:57
 * To change this template use File | Settings | File Templates.
 */
public class StartCommand extends AbstractCommand {
    private final IGameConfiguration configuration;
    private final IBaseEntityRepository<Map, Integer> maps;

    public StartCommand(IGameConfiguration configuration, IBaseEntityRepository<Map, Integer> maps) {
        this.configuration = configuration;
        this.maps = maps;
    }

    @Override
    public String helpMessage() {
        return "Teleport you to your saved position.";
    }

    @Override
    public String[] usageParameters() {
        return new String[0];
    }

    @Override
    public String name() {
        return "start";
    }

    @Override
    public Permissions level() {
        return Permissions.MEMBER;
    }

    @Override
    public void parse(GameClient client, DofusLogger out, String[] args) {
        try {
            RolePlayMovement.teleport(client,
                    maps.findById(configuration.getStartMapId()),
                    configuration.getStartCellId()
            );
        } catch (GameActionException e) {
            out.error("Can't teleport you because : {0}", e.getMessage());
        }
    }
}
