package org.d2j.game.game.commands;

import org.d2j.common.Permissions;
import org.d2j.game.game.logging.DofusLogger;
import org.d2j.game.service.IWorld;
import org.d2j.game.service.game.GameClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 25/01/12
 * Time: 22:20
 * To change this template use File | Settings | File Templates.
 */
public class SaveCommand extends AbstractCommand {
    private static final Logger logger = LoggerFactory.getLogger(SaveCommand.class);

    private final IWorld world;

    public SaveCommand(IWorld world) {
        this.world = world;
    }

    @Override
    public String helpMessage() {
        return "Save dynamic datas.";
    }

    @Override
    public String[] usageParameters() {
        return new String[0];
    }

    @Override
    public String name() {
        return "save";
    }

    @Override
    public Permissions level() {
        return Permissions.ADMINISTRATOR;
    }

    @Override
    public void parse(GameClient client, DofusLogger out, String[] args) {
        try {
            world.getRepositoryManager().save();
        } catch (SQLException e) {
            out.error("Can't save dynamic context because : {0}", e.getMessage());
            logger.warn(e.getMessage(), e.getCause());
        }
    }
}
