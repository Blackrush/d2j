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
 * Time: 17:24
 * To change this template use File | Settings | File Templates.
 */
public class AddNpcSellCommand extends AbstractCommand {
    private final IRepositoryManager repo;

    public AddNpcSellCommand(IRepositoryManager repo) {
        this.repo = repo;
    }

    @Override
    public String helpMessage() {
        return "Add an item in the sell list of the npc.";
    }

    @Override
    public String[] usageParameters() {
        return new String[]{"ID of NpcTemplate", "ID of ItemTemplate"};
    }

    @Override
    public String name() {
        return "add_npc_sell";
    }

    @Override
    public Permissions level() {
        return Permissions.GAME_MASTER;
    }

    @Override
    public void parse(GameClient client, DofusLogger out, String[] args) {
        if (args.length != 2){
            out.error("Usage: " + usage());
        }
        else{
            NpcTemplate npc = repo.getNpcTemplates().findById(Integer.parseInt(args[0]));
            ItemTemplate item = repo.getItemTemplates().findById(Integer.parseInt(args[1]));

            if (npc == null){
                out.error("Unknown npc {0}.", args[0]);
            }
            else if (item == null){
                out.error("Unknown item {0}.", args[1]);
            }
            else{
                npc.getSells().add(item);

                out.info("You successfully set {0} in the sell list.", item.getName());
            }
        }
    }
}
