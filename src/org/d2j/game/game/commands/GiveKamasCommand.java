package org.d2j.game.game.commands;

import org.d2j.common.Permissions;
import org.d2j.common.client.protocol.InfoGameMessageFormatter;
import org.d2j.game.game.logging.DofusLogger;
import org.d2j.game.model.*;
import org.d2j.game.model.Character;
import org.d2j.game.repository.CharacterRepository;
import org.d2j.game.service.game.GameClient;
import org.d2j.utils.database.repository.IEntityRepository;

import static org.d2j.common.client.protocol.InfoGameMessageFormatter.urlize;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 05/02/12
 * Time: 20:12
 * To change this template use File | Settings | File Templates.
 */
public class GiveKamasCommand extends AbstractCommand {
    private final CharacterRepository characters;

    public GiveKamasCommand(CharacterRepository characters) {
        this.characters = characters;
    }

    @Override
    public String helpMessage() {
        return "Give kamas to the target.";
    }

    @Override
    public String[] usageParameters() {
        return new String[]{"KAMAS", "ID or NAME of Character|facultative"};
    }

    @Override
    public String name() {
        return "kamas";
    }

    @Override
    public Permissions level() {
        return Permissions.QUIZ_MASTER;
    }

    @Override
    public void parse(GameClient client, DofusLogger out, String[] args) {
        if (args.length < 1 || args.length > 2){
            out.error("Usage: " + usage());
        }
        else{
            int kamas = Integer.parseInt(args[0]);
            Character target;
            if (args.length > 1){
                target = characters.findByIdOrName(args[1]);
            }
            else{
                target = client.getCharacter();
            }

            if (target == null){
                out.error("Unknown target {0}.", args[1]);
            }
            else{
                target.getBag().addKamas(kamas);
                if (target.getOwner().isOnline()){
                    target.getOwner().getClient().getSession().write(target.getStatistics().getStatisticsMessage());

                    target.getOwner().getClient().getTchatOut().info("{0} gave you {1} kamas.",
                            urlize(client.getCharacter().getName()),
                            kamas
                    );
                }

                out.info("You successfully gave {0} kamas to {1}.", kamas, target.getName());
            }
        }
    }
}
