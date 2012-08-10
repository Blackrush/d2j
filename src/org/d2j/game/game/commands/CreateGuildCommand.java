package org.d2j.game.game.commands;

import org.d2j.common.Permissions;
import org.d2j.common.client.protocol.GuildGameMessageFormatter;
import org.d2j.game.game.logging.DofusLogger;
import org.d2j.game.model.Character;
import org.d2j.game.repository.CharacterRepository;
import org.d2j.game.service.game.GameClient;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 04/02/12
 * Time: 19:32
 * To change this template use File | Settings | File Templates.
 */
public class CreateGuildCommand extends AbstractCommand {
    private final CharacterRepository characters;

    public CreateGuildCommand(CharacterRepository characters) {
        this.characters = characters;
    }

    @Override
    public String helpMessage() {
        return "Open the panel for create a guild.";
    }

    @Override
    public String[] usageParameters() {
        return new String[]{"ID or NAME of Character|facultative"};
    }

    @Override
    public String name() {
        return "guild";
    }

    @Override
    public Permissions level() {
        return Permissions.QUIZ_MASTER;
    }

    @Override
    public void parse(GameClient client, DofusLogger out, String[] args) {
        Character target;
        if (args.length > 0){
            target = characters.findByIdOrName(args[0]);
        }
        else{
            target = client.getCharacter();
        }

        if (target == null){
            out.error("Unknown target {0}.", args[0]);
        }
        else if (!target.getOwner().isOnline()){
            out.error("\"{0}\" is not online.", target.getName());
        }
        else if (target.getGuildData() != null){
            out.error("\"{0}\" has a guild.", target.getName());
        }
        else if (target.getOwner().getClient().isBusy()){
            out.error("\"{0}\" is busy.", target.getName());
        }
        else{
            target.getOwner().getClient().getSession().write(GuildGameMessageFormatter.startCreationMessage());
        }
    }
}
