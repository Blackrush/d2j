package org.d2j.game.game.live_actions.actions;

import org.d2j.game.game.actions.GameActionException;
import org.d2j.game.game.actions.RolePlayMovement;
import org.d2j.game.game.live_actions.ILiveAction;
import org.d2j.game.game.live_actions.LiveActionType;
import org.d2j.game.service.game.GameClient;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 11/02/12
 * Time: 18:43
 * To change this template use File | Settings | File Templates.
 */
public class TeleportBackAction implements ILiveAction {
    private String message;

    public TeleportBackAction() {
    }

    public TeleportBackAction(String message) {
        this.message = message;
    }

    @Override
    public LiveActionType getResponseType() {
        return LiveActionType.TELEPORT_BACK;
    }

    @Override
    public void apply(GameClient client) throws GameActionException {
        RolePlayMovement.teleport(
                client,
                client.getCharacter().getMemorizedMap(),
                client.getCharacter().getMemorizedCell()
        );

        if (message != null && !message.isEmpty()){
            client.getTchatOut().log(message);
        }
    }
}
