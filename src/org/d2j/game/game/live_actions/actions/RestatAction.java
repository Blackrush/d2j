package org.d2j.game.game.live_actions.actions;

import org.d2j.game.configuration.IGameConfiguration;
import org.d2j.game.game.actions.GameActionException;
import org.d2j.game.game.actions.NpcDialogAction;
import org.d2j.game.game.live_actions.ILiveAction;
import org.d2j.game.game.live_actions.LiveActionType;
import org.d2j.game.service.game.GameClient;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 30/01/12
 * Time: 22:38
 * To change this template use File | Settings | File Templates.
 */
public class RestatAction implements ILiveAction {
    private final IGameConfiguration configuration;

    public RestatAction(IGameConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public LiveActionType getResponseType() {
        return LiveActionType.RESTAT;
    }

    @Override
    public void apply(GameClient client) throws GameActionException {
        client.getCharacter().getStatistics().restat(configuration);

        client.getSession().write(client.getCharacter().getStatistics().getStatisticsMessage());
    }
}
