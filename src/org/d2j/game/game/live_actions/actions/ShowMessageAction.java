package org.d2j.game.game.live_actions.actions;

import org.d2j.game.game.actions.GameActionException;
import org.d2j.game.game.actions.NpcDialogAction;
import org.d2j.game.game.live_actions.ILiveAction;
import org.d2j.game.game.live_actions.LiveActionType;
import org.d2j.game.service.game.GameClient;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 11/02/12
 * Time: 16:45
 * To change this template use File | Settings | File Templates.
 */
public class ShowMessageAction implements ILiveAction {
    private String message;

    public ShowMessageAction(String message) {
        this.message = message;
    }

    @Override
    public LiveActionType getResponseType() {
        return LiveActionType.SHOW_MESSAGE;
    }

    @Override
    public void apply(GameClient client) throws GameActionException {
        client.getTchatOut().log(message);
    }
}
