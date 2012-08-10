package org.d2j.game.game.live_actions.actions;

import org.d2j.common.client.protocol.enums.OrientationEnum;
import org.d2j.game.game.actions.GameActionException;
import org.d2j.game.game.actions.NpcDialogAction;
import org.d2j.game.game.actions.RolePlayMovement;
import org.d2j.game.game.live_actions.ILiveAction;
import org.d2j.game.game.live_actions.LiveActionType;
import org.d2j.game.model.Map;
import org.d2j.game.service.game.GameClient;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 01/02/12
 * Time: 14:38
 * To change this template use File | Settings | File Templates.
 */
public class TeleportAction implements ILiveAction {
    private final Map map;
    private final short cellid;
    private final OrientationEnum orientation;

    public TeleportAction(Map map, short cellid, OrientationEnum orientation) {
        this.map = map;
        this.cellid = cellid;
        this.orientation = orientation;
    }

    @Override
    public LiveActionType getResponseType() {
        return LiveActionType.TELEPORT;
    }

    @Override
    public void apply(GameClient client) throws GameActionException {
        RolePlayMovement.teleport(client, map, cellid, orientation);
    }
}
