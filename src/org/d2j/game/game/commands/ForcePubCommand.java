package org.d2j.game.game.commands;

import org.d2j.common.Permissions;
import org.d2j.game.game.PubSystem;
import org.d2j.game.game.logging.DofusLogger;
import org.d2j.game.service.game.GameClient;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 05/02/12
 * Time: 20:51
 * To change this template use File | Settings | File Templates.
 */
public class ForcePubCommand extends AbstractCommand {
    private final PubSystem pubSystem;

    public ForcePubCommand(PubSystem pubSystem) {
        this.pubSystem = pubSystem;
    }

    @Override
    public String helpMessage() {
        return "Show pub message.";
    }

    @Override
    public String[] usageParameters() {
        return new String[]{"ID PUB"};
    }

    @Override
    public String name() {
        return "pub_force";
    }

    @Override
    public Permissions level() {
        return Permissions.QUIZ_MASTER;
    }

    @Override
    public void parse(GameClient client, DofusLogger out, String[] args) {
        if (args.length < 1){
            out.error("Usage: " + usage());
        }
        else{
            pubSystem.force(Integer.parseInt(args[0]));
        }
    }
}
