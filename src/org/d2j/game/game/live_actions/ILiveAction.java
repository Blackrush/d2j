package org.d2j.game.game.live_actions;

import org.d2j.game.game.actions.GameActionException;
import org.d2j.game.game.actions.NpcDialogAction;
import org.d2j.game.service.game.GameClient;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 30/01/12
 * Time: 20:16
 * To change this template use File | Settings | File Templates.
 */
public interface ILiveAction {
    LiveActionType getResponseType();

    void apply(GameClient client) throws GameActionException;
}
