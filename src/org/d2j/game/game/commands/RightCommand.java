package org.d2j.game.game.commands;

import org.d2j.common.Permissions;
import org.d2j.game.game.logging.DofusLogger;
import org.d2j.game.model.GameAccount;
import org.d2j.game.repository.AccountRepository;
import org.d2j.game.service.game.GameClient;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 26/01/12
 * Time: 17:46
 * To change this template use File | Settings | File Templates.
 */
public class RightCommand extends AbstractCommand {
    private final AccountRepository accounts;

    public RightCommand(AccountRepository accounts) {
        this.accounts = accounts;
    }

    @Override
    public String helpMessage() {
        return "Edit the target's rights.";
    }

    @Override
    public String[] usageParameters() {
        return new String[]{"ID or NICKNAME of GameAccount", "RIGHT:int"};
    }

    @Override
    public String name() {
        return "right";
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
            GameAccount target = accounts.findByIdOrNickname(args[0]);
            Permissions rights = Permissions.valueOf(Integer.parseInt(args[1]));

            if (target == null){
                out.error("Unknown target {0}.", args[0]);
            }
            else if (rights == null){
                out.error("Unknown rights {0}.", args[1]);
            }
            else{
                target.setRights(rights);

                if (target.isOnline()){
                    target.getClient().getTchatOut().info("You've been promoted to {0}.", rights.toString());
                }

                out.info("{0} has been promoted to {1}.", target.getNickname(), rights);
            }
        }
    }
}
