package org.d2j.game.game.commands;

import org.d2j.common.Permissions;
import org.d2j.common.StringUtils;
import org.d2j.game.game.events.SystemMessageEvent;
import org.d2j.game.game.logging.DofusLogger;
import org.d2j.game.model.*;
import org.d2j.game.model.Character;
import org.d2j.game.repository.CharacterRepository;
import org.d2j.game.service.IWorld;
import org.d2j.game.service.game.GameClient;
import org.d2j.game.service.login.ILoginServerManager;

import static org.d2j.common.StringUtils.makeSentence;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 21/01/12
 * Time: 14:08
 * To change this template use File | Settings | File Templates.
 */
public class BanCommand extends AbstractCommand {
    private static final String PATTERN = "(Information) {0} a été bannis car : {1}";

    private final IWorld world;

    public BanCommand(IWorld world) {
        this.world = world;
    }

    @Override
    public String helpMessage() {
        return "Ban the target.";
    }

    @Override
    public String[] usageParameters() {
        return new String[]{"ID or NAME of Character", "REASON:facultative"};
    }

    @Override
    public String name() {
        return "ban";
    }

    @Override
    public Permissions level() {
        return Permissions.GAME_MASTER;
    }

    @Override
    public void parse(GameClient client, DofusLogger out, String[] args) {
        if (args.length < 1 || args.length > 2){
            out.error("Usage: " + usage());
        }
        else{
            Character target = world.getRepositoryManager().getCharacters().findByIdOrName(args[0]);
            if (target == null){
                out.error("Unknown target {0}.", args[0]);
            }
            else{
                target.getOwner().setRights(Permissions.BANNED);
                world.getLoginServerManager().refreshGameAccount(target.getOwner());

                if (target.getOwner().isOnline()){
                    target.getOwner().getClient().kick();
                }

                out.info("You've banned \"{0}\".", target.getName());

                if (args.length > 1){
                    String reason = makeSentence(args, 1);

                    world.systemMessage(new SystemMessageEvent(StringUtils.format(
                            PATTERN,
                            target.getName(),
                            reason
                    )));
                }
            }
        }
    }
}
