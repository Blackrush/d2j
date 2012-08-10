package org.d2j.game.game.live_actions.actions;

import org.d2j.common.random.Dice;
import org.d2j.game.game.actions.GameActionException;
import org.d2j.game.game.live_actions.ILiveAction;
import org.d2j.game.game.live_actions.LiveActionType;
import org.d2j.game.service.game.GameClient;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 11/02/12
 * Time: 18:13
 * To change this template use File | Settings | File Templates.
 */
public class AddLifeAction implements ILiveAction {
    private Dice dice;
    private String message;

    public AddLifeAction(Dice dice, String message) {
        this.dice = dice;
        this.message = message;
    }

    @Override
    public LiveActionType getResponseType() {
        return LiveActionType.ADD_LIFE;
    }

    @Override
    public void apply(GameClient client) throws GameActionException {
        short life = client.getCharacter().getStatistics().addLife((short) dice.roll());

        client.getSession().write(client.getCharacter().getStatistics().getStatisticsMessage());

        if (message != null && !message.isEmpty()) {
            client.getTchatOut().log("Vous regagnez {0} points de vie.", life);
        }
    }
}
