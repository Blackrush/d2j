package org.d2j.game.game.commands;

import org.d2j.common.Permissions;
import org.d2j.game.IRepositoryManager;
import org.d2j.game.game.logging.DofusLogger;
import org.d2j.game.model.ItemTemplate;
import org.d2j.game.model.NpcTemplate;
import org.d2j.game.service.game.GameClient;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 02/02/12
 * Time: 17:28
 * To change this template use File | Settings | File Templates.
 */
public class RemoveNpcSellCommand extends AbstractCommand {
    private final IRepositoryManager repo;

    public RemoveNpcSellCommand(IRepositoryManager repo) {
        this.repo = repo;
    }

    @Override
    public String helpMessage() {
        return "Remove an item from the npc's sell list.";
    }

    @Override
    public String[] usageParameters() {
        return new String[]{"ID of NpcTemplate", "ID of ItemTemplate"};
    }

    @Override
    public String name() {
        return "remove_npc_sell";
    }

    @Override
    public Permissions level() {
        return Permissions.GAME_MASTER;
    }

    @Override
    public void parse(GameClient client, DofusLogger out, String[] args) {
        if (args.length < 2){
            out.error("Usage: " + usage());
        }
        else{
            NpcTemplate npc = repo.getNpcTemplates().findById(Integer.parseInt(args[0]));

            if (npc.getSells().remove(Integer.parseInt(args[1]))){
                out.info("You have successfully remove the item #{0} from the npc's sell list.", args[1]);
            }
            else{
                out.error("Unknown item #{0} in the sell list.", args[1]);
            }
        }
    }
}
