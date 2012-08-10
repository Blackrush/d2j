package org.d2j.game.game.trades;

import org.d2j.game.game.actions.GameActionException;
import org.d2j.game.game.actions.IGameAction;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 26/02/12
 * Time: 12:25
 * To change this template use File | Settings | File Templates.
 */
public interface PurchasableTrade extends IGameAction {
    void buy(long itemId, int quantity) throws GameActionException;
}
