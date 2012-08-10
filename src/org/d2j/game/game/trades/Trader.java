package org.d2j.game.game.trades;

import org.d2j.game.game.actions.GameActionException;
import org.d2j.game.game.items.Bag;
import org.d2j.game.service.game.GameClient;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 14/01/12
 * Time: 11:28
 * To change this template use File | Settings | File Templates.
 */
public class Trader {
    private final PlayerTrade trade;
    private final GameClient client, opposite;
    private final Bag bag;
    private boolean ready;

    public Trader(PlayerTrade trade, GameClient client, GameClient opposite) {
        this.trade = trade;
        this.client = client;
        this.opposite = opposite;
        this.bag = new Bag(this.client.getCharacter());
        this.ready = false;
    }

    public PlayerTrade getTrade() {
        return trade;
    }

    public GameClient getClient() {
        return client;
    }

    public GameClient getOpposite() {
        return opposite;
    }

    public Bag getBag() {
        return bag;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady() throws GameActionException {
        ready = !ready;
        trade.setReady(this, ready);
    }
}
