package org.d2j.game.game.commands;

import org.d2j.common.Permissions;
import org.d2j.game.game.PubSystem;
import org.d2j.game.game.logging.DofusLogger;
import org.d2j.game.service.game.GameClient;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 05/02/12
 * Time: 21:12
 * To change this template use File | Settings | File Templates.
 */
public class EditPubDelayCommand extends AbstractCommand {
    private final PubSystem pubSystem;

    public EditPubDelayCommand(PubSystem pubSystem) {
        this.pubSystem = pubSystem;
    }

    @Override
    public String helpMessage() {
        return "Edit pub delay between two messages.";
    }

    @Override
    public String[] usageParameters() {
        return new String[]{"DELAY (minutes)"};
    }

    @Override
    public String name() {
        return "pub_delay";
    }

    @Override
    public Permissions level() {
        return Permissions.ADMINISTRATOR;
    }

    @Override
    public void parse(GameClient client, DofusLogger out, String[] args) {
        if (args.length < 1){
            out.error("Usage: " + usage());
        }
        else{
            int delay = Integer.parseInt(args[0]);
            pubSystem.setInterval(delay);
        }
    }
}
