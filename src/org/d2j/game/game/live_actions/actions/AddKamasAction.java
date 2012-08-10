package org.d2j.game.game.live_actions.actions;

import org.d2j.game.game.actions.GameActionException;
import org.d2j.game.game.live_actions.ILiveAction;
import org.d2j.game.game.live_actions.LiveActionType;
import org.d2j.game.service.game.GameClient;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 11/02/12
 * Time: 19:09
 * To change this template use File | Settings | File Templates.
 */
public class AddKamasAction implements ILiveAction {
    private long kamas;

    public AddKamasAction(long kamas) {
        this.kamas = kamas;
    }

    @Override
    public LiveActionType getResponseType() {
        return LiveActionType.ADD_KAMAS;
    }

    @Override
    public void apply(GameClient client) throws GameActionException {
        client.getCharacter().getBag().addKamas(kamas);

        client.getSession().write(client.getCharacter().getStatistics().getStatisticsMessage());

        client.getTchatOut().log("Vous avez gagné {0} kamas.", kamas);
    }
}
