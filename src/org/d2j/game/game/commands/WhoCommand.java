package org.d2j.game.game.commands;

import org.d2j.common.Permissions;
import org.d2j.game.game.logging.DofusLogger;
import org.d2j.game.model.Character;
import org.d2j.game.model.GameAccount;
import org.d2j.game.service.IWorld;
import org.d2j.game.service.game.GameClient;
import org.d2j.utils.AbstractCacheSystem;
import org.d2j.utils.DelegateTimerCacheSystem;
import org.d2j.utils.Maker;
import org.joda.time.Duration;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 19/01/12
 * Time: 18:09
 * To change this template use File | Settings | File Templates.
 */
public class WhoCommand extends AbstractCommand {
    private final IWorld world;

    public WhoCommand(IWorld world) {
        this.world = world;
    }

    @Override
    public String helpMessage() {
        return "Show online players.";
    }

    @Override
    public String[] usageParameters() {
        return new String[0];
    }

    @Override
    public String name() {
        return "who";
    }

    @Override
    public Permissions level() {
        return Permissions.QUIZ_MASTER;
    }

    @Override
    public void parse(GameClient client, DofusLogger out, String[] args) {
        if (args.length > 0){
            for (String raw : args){
                GameAccount account = world.getRepositoryManager().getAccounts().findByIdOrNickname(raw);
                if (account == null){
                    Character character = world.getRepositoryManager().getCharacters().findByIdOrName(raw);
                    if (character == null){
                        out.info("Unknown account or character \"{0}\".", raw);
                        return;
                    }
                    account = character.getOwner();
                }

                if (account.isOnline()){
                    out.info("{0} ({1}) is online. He plays with {2}.",
                            account.getNickname(),
                            account.getId(),
                            account.getClient().getCharacter().urlize()
                    );
                }
                else{
                    out.info("{0} ({1}) is not online.",
                            account.getNickname(),
                            account.getId()
                    );
                }
            }
        }
        else{
            Collection<Character> onlines = world.getOnlinePlayers();

            StringBuilder sb = new StringBuilder();
            sb.append("====================").append('\n');
            sb.append("There are ").append(onlines.size()).append(" online players :").append('\n');

            for (Character character : onlines){
                sb.append(" - ");
                sb.append(character).append('\n');
            }

            sb.append("====================");

            out.info(sb.toString());
        }
    }
}
