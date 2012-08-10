package org.d2j.game.game.actions;

import org.d2j.common.NetworkStringBuffer;
import org.d2j.common.client.protocol.InfoGameMessageFormatter;
import org.d2j.common.client.protocol.WaypointGameMessageFormatter;
import org.d2j.game.model.Waypoint;
import org.d2j.game.service.game.GameClient;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 22/02/12
 * Time: 19:46
 * To change this template use File | Settings | File Templates.
 */
public class WaypointPanelAction implements IGameAction {
    private final GameClient client;

    public WaypointPanelAction(GameClient client) {
        this.client = client;
    }

    @Override
    public GameActionType getActionType() {
        return GameActionType.WAYPOINT_PANEL;
    }

    @Override
    public void begin() throws GameActionException {
        Waypoint currentWaypoint = client.getCharacter().getCurrentMap().getWaypoint();

        try (NetworkStringBuffer buf = new NetworkStringBuffer(client.getSession())){
            if (!client.getCharacter().getWaypoints().contains(currentWaypoint)){
                client.getCharacter().getWaypoints().add(currentWaypoint);

                buf.append(InfoGameMessageFormatter.newZaapMessage());
            }

            buf.append(WaypointGameMessageFormatter.listMessage(
                    client.getCharacter().getCurrentMap().getId(),
                    client.getCharacter().getWaypoints().toTuples()
            ));
        }
    }

    @Override
    public void end() throws GameActionException {
        client.getSession().write(WaypointGameMessageFormatter.closePanelMessage());
    }

    @Override
    public void cancel() throws GameActionException {
        end();
    }

    public void use(Waypoint waypoint) throws GameActionException {
        long cost = waypoint.getCost(client.getCharacter().getCurrentMap());
        if (cost > client.getCharacter().getBag().getKamas()){
            throw new GameActionException("You have not got enough kamas.");
        }

        client.getCharacter().getBag().addKamas(-cost);

        try (NetworkStringBuffer buf = new NetworkStringBuffer(client.getSession())){
            buf.append(client.getCharacter().getStatistics().getStatisticsMessage());
            buf.append(WaypointGameMessageFormatter.closePanelMessage());
        }

        RolePlayMovement.teleport(client, waypoint.getMap(), waypoint.getCellId());
    }
}
