package org.d2j.game.game.commands;

import org.d2j.common.Permissions;
import org.d2j.common.StringUtils;
import org.d2j.common.client.protocol.ChannelGameMessageFormatter;
import org.d2j.game.game.logging.DofusLogger;
import org.d2j.game.model.*;
import org.d2j.game.model.Character;
import org.d2j.game.service.IWorld;
import org.d2j.game.service.game.GameClient;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 24/01/12
 * Time: 21:00
 * To change this template use File | Settings | File Templates.
 */
public class ReportCommand extends AbstractCommand {
    private final IWorld world;

    public ReportCommand(IWorld world) {
        this.world = world;
    }

    @Override
    public String helpMessage() {
        return "Send a message for each online administrator.";
    }

    @Override
    public String[] usageParameters() {
        return new String[]{"MESSAGE"};
    }

    @Override
    public String name() {
        return "report";
    }

    @Override
    public Permissions level() {
        return Permissions.MEMBER;
    }

    @Override
    public void parse(GameClient client, DofusLogger out, String[] args) {
        if (args.length < 1){
            out.error("Usage: " + usage());
        }
        else{
            Collection<Character> staff = world.getOnlineStaff();

            if (staff.size() > 0){
                String message = StringUtils.makeSentence(args);

                for (Character character : staff){
                    character.getOwner().getClient().getSession().write(ChannelGameMessageFormatter.clientPrivateMessage(
                            false,
                            client.getCharacter().getId(),
                            client.getCharacter().getName(),
                            message
                    ));
                }

                out.info("Your message has been sended.");
            }
            else{
                out.error("There isn't online administrator.");
            }
        }
    }
}
