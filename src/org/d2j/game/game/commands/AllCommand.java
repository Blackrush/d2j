package org.d2j.game.game.commands;

import org.d2j.game.service.IWorld;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 21/01/12
 * Time: 18:17
 * To change this template use File | Settings | File Templates.
 */
public class AllCommand extends NameAnnounceCommand {
    public AllCommand(IWorld world) {
        super(world);
    }

    @Override
    public String name() {
        return "all";
    }
}
