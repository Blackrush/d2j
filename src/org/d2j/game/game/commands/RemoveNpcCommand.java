package org.d2j.game.game.commands;

import org.d2j.common.Permissions;
import org.d2j.game.game.logging.DofusLogger;
import org.d2j.game.model.Npc;
import org.d2j.game.repository.NpcRepository;
import org.d2j.game.service.game.GameClient;
import org.d2j.utils.database.repository.IBaseEntityRepository;
import org.d2j.utils.database.repository.IEntityRepository;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 27/01/12
 * Time: 17:20
 * To change this template use File | Settings | File Templates.
 */
public class RemoveNpcCommand extends AbstractCommand {
    private final IEntityRepository<Npc, Integer> npcs;

    public RemoveNpcCommand(IEntityRepository<Npc, Integer> npcs) {
        this.npcs = npcs;
    }

    @Override
    public String helpMessage() {
        return "Remove a NPC by its id.";
    }

    @Override
    public String[] usageParameters() {
        return new String[]{"ID of Npc"};
    }

    @Override
    public String name() {
        return "del_npc";
    }

    @Override
    public Permissions level() {
        return Permissions.GAME_MASTER;
    }

    @Override
    public void parse(GameClient client, DofusLogger out, String[] args) {
        if (args.length < 1){
            out.error("Usage: " + usage());
        }
        else{
            if (npcs.delete(Integer.parseInt(args[0]))){
                out.info("NPC #{0} successfully removed.", args[0]);
            }
            else{
                out.error("Unknown NPC \"{0}\".", args[0]);
            }
        }
    }
}
