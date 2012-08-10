package org.d2j.game.game.commands;

import org.d2j.common.Permissions;
import org.d2j.game.game.RolePlayActor;
import org.d2j.game.game.logging.DofusLogger;
import org.d2j.game.model.Map;
import org.d2j.game.model.MapTrigger;
import org.d2j.game.model.Npc;
import org.d2j.game.service.game.GameClient;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 08/02/12
 * Time: 19:26
 * To change this template use File | Settings | File Templates.
 */
public class MapInformationsCommand extends AbstractCommand {
    @Override
    public String helpMessage() {
        return "Show informations of your current map.";
    }

    @Override
    public String[] usageParameters() {
        return new String[0];
    }

    @Override
    public String name() {
        return "map_infos";
    }

    @Override
    public Permissions level() {
        return Permissions.GAME_MASTER;
    }

    @Override
    public void parse(GameClient client, DofusLogger out, String[] args) {
        Map map = client.getCharacter().getCurrentMap();

        StringBuilder sb = new StringBuilder();

        sb.append("ID = ").append(map.getId()).append('\n');

        sb.append("POSITION = (").append(map.getAbscissa()).append(';').append(map.getOrdinate()).append(")\n");

        sb.append("SUBSCRIBER AREA = ").append(map.isSubscriberArea()).append('\n');

        sb.append("NB CELLS = ").append(map.getCells().length).append('\n');

        sb.append("TRIGGERS = \n");
        for (MapTrigger trigger : map.getTriggers().values()){
            sb.append("  * CELL = ").append(trigger.getCellId()).append("; ");
            sb.append("LOCATION = ").append(trigger.getNextMap().getId()).append(", ").append(trigger.getNextCellId());
            sb.append('\n');
        }

        sb.append("NB FIGTHS = ").append(map.getNbFights()).append('\n');

        sb.append("NPCS = \n");
        for (RolePlayActor actor : map.getActors()){
            if (!(actor instanceof Npc)) continue;
            Npc npc = (Npc)actor;

            sb.append("  * ID = ").append(npc.getId()).append("; PUBLIC ID = ").append(npc.getPublicId()).append("; ");
            sb.append("TEMPLATE ID = ").append(npc.getTemplate().getId()).append("; ");
            sb.append("CELL = ").append(npc.getCurrentCell()).append("; ");
            sb.append("QUESTION = ").append(npc.getTemplate().getQuestion() != null ? npc.getTemplate().getQuestion().getId() : "?").append('\n');
        }

        sb.append("NB PLAYERS = ").append(map.getActors().size()).append('\n');

        sb.append("==========================");

        out.info(sb.toString());
    }
}
