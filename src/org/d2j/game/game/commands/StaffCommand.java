package org.d2j.game.game.commands;

import org.d2j.common.Permissions;
import org.d2j.common.client.protocol.InfoGameMessageFormatter;
import org.d2j.game.game.logging.DofusLogger;
import org.d2j.game.model.*;
import org.d2j.game.model.Character;
import org.d2j.game.service.IWorld;
import org.d2j.game.service.game.GameClient;
import org.d2j.utils.Predicate;
import org.d2j.utils.database.repository.IBaseEntityRepository;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 19/01/12
 * Time: 18:17
 * To change this template use File | Settings | File Templates.
 */
public class StaffCommand extends AbstractCommand {
    private final IWorld world;

    public StaffCommand(IWorld world) {
        this.world = world;
    }

    @Override
    public String helpMessage() {
        return "Show online staff's members.";
    }

    @Override
    public String[] usageParameters() {
        return new String[0];
    }

    @Override
    public String name() {
        return "staff";
    }

    @Override
    public Permissions level() {
        return Permissions.MEMBER;
    }

    @Override
    public void parse(GameClient client, DofusLogger out, String[] args) {
        Collection<Character> onlines = world.getOnlineStaff();

        StringBuilder sb = new StringBuilder();

        sb.append("There are ").append(onlines.size()).append(" online staff's member :").append('\n');

        for (Character character : onlines){
            sb.append(" - ");
            sb.append(InfoGameMessageFormatter.urlize(character.getName())).append('\n');
        }

        sb.append("====================");

        out.info(sb.toString());
    }
}
