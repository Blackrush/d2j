package org.d2j.game.game.spells.cell_effects;

import org.d2j.game.game.fights.AppendableFightHandlerAction;
import org.d2j.game.game.fights.FightCell;
import org.d2j.game.game.fights.FightException;
import org.d2j.game.game.fights.IFighter;

/**
 * Created by IntelliJ IDEA.
 * User: Blackrush
 * Date: 07/03/12
 * Time: 14:25
 */
public interface CellEffect {
    FightCell getCell();

    void add();
    void delete();

    void onFighterWalkedOn(AppendableFightHandlerAction action, IFighter trigger) throws FightException;
    void onTurnStopped(AppendableFightHandlerAction action) throws FightException;
}
