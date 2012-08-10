package org.d2j.game.game.commands;

import org.d2j.common.Permissions;
import org.d2j.game.IRepositoryManager;
import org.d2j.game.game.logging.DofusLogger;
import org.d2j.game.model.Npc;
import org.d2j.game.model.NpcTemplate;
import org.d2j.game.service.game.GameClient;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 27/01/12
 * Time: 17:12
 * To change this template use File | Settings | File Templates.
 */
public class AddNpcCommand extends AbstractCommand {
    private final IRepositoryManager repo;

    public AddNpcCommand(IRepositoryManager repo) {
        this.repo = repo;
    }

    @Override
    public String helpMessage() {
        return "Add a NPC on your position.";
    }

    @Override
    public String[] usageParameters() {
        return new String[]{"ID of NpcTemplate"};
    }

    @Override
    public String name() {
        return "add_npc";
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
            NpcTemplate tpl = repo.getNpcTemplates().findById(Integer.parseInt(args[0]));
            if (tpl == null){
                out.error("Can't find NpcTemplate \"{0}\".", args[0]);
            }
            else{
                Npc npc = new Npc(
                        tpl,
                        client.getCharacter().getCurrentMap(),
                        client.getCharacter().getCurrentCell(),
                        client.getCharacter().getCurrentOrientation()
                );

                repo.getNpcs().create(npc);

                out.info("NPC #{0} created on your position (public id = {1}).", npc.getId(), npc.getPublicId());
            }
        }
    }
}
