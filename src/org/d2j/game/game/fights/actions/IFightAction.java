package org.d2j.game.game.fights.actions;

import org.d2j.game.game.fights.FightException;
import org.d2j.game.game.fights.IFighter;
import org.d2j.utils.Future;

/**
 * User: Blackrush
 * Date: 24/11/11
 * Time: 17:56
 * IDE : IntelliJ IDEA
 */
public interface IFightAction {
    boolean init() throws FightException;
    void begin() throws FightException;
    void end() throws FightException;

    Future<IFightAction> getEndFuture();

    IFighter getFighter();
}
