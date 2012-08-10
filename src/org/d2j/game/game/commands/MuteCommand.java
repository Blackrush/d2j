package org.d2j.game.game.commands;

import org.d2j.common.Permissions;
import org.d2j.game.game.logging.DofusLogger;
import org.d2j.game.model.*;
import org.d2j.game.model.Character;
import org.d2j.game.repository.CharacterRepository;
import org.d2j.game.service.game.GameClient;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 09/01/12
 * Time: 18:58
 * To change this template use File | Settings | File Templates.
 */
public class MuteCommand extends AbstractCommand {
    private CharacterRepository characters;

    public MuteCommand(CharacterRepository characters) {
        this.characters = characters;
    }

    @Override
    public String name() {
        return "mute";
    }

    @Override
    public String helpMessage() {
        return "Mute a player.";
    }

    @Override
    public String[] usageParameters() {
        return new String[]{
                "ID or NAME of Character",
                "DELAY (minutes):facultative"
        };
    }

    @Override
    public Permissions level() {
        return Permissions.GAME_MASTER;
    }

    @Override
    public void parse(final GameClient client, DofusLogger out, String[] args) {
        if (args.length < 1 || args.length > 2){
            out.error("Usage: " + usage());
        }
        else{
            final Character target = characters.findByIdOrName(args[0]);

            if (target == null){
                out.error("Unknown target {0}.", args[0]);
            }
            else if (target.getOwner().isMuted()){
                out.error("Target {0} is already muted.", target.getName());
            }
            else{ //todo: little piece of fucking shit
                if (args.length > 1 && target.getOwner().isOnline()){
                    final int delay = Integer.parseInt(args[1]);

                    target.getOwner().setMuted(true);
                    target.getOwner().getClient().getTchatOut().info("You have been muted by {0} for {1} minutes.", client.getCharacter().getName(), delay);
                    out.info("{0} is successfully muted for {1} minutes.", target.getName(), delay);

                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            if (!target.getOwner().isOnline() || !target.getOwner().isMuted()) return;

                            target.getOwner().setMuted(false);
                            target.getOwner().getClient().getTchatOut().info("You are now demuted.");
                            client.getTchatOut().info("{0} is now demuted (delay: {1} minutes).", target.getName(), delay);
                        }
                    }, delay * 60000);
                }
                else{
                    target.getOwner().setMuted(true);
                    if (target.getOwner().isOnline()){
                        target.getOwner().getClient().getTchatOut().info("You have been muted by {0}.", client.getCharacter().getName());
                    }
                    out.info("{0} is successfully muted.", target.getName());
                }
            }
        }
    }
}
