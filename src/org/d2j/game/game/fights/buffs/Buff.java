package org.d2j.game.game.fights.buffs;

import org.d2j.common.client.protocol.enums.SpellEffectsEnum;
import org.d2j.game.game.fights.AppendableFightHandlerAction;
import org.d2j.game.game.fights.FightException;
import org.d2j.game.game.fights.IFighter;
import org.d2j.game.model.SpellTemplate;

/**
 * Created by IntelliJ IDEA.
 * User: Blackrush
 * Date: 01/03/12
 * Time: 20:37
 */
public interface Buff {
    void onAdded(AppendableFightHandlerAction action) throws FightException;
    void onRemoved(AppendableFightHandlerAction action) throws FightException;
    void onTurnStarted(AppendableFightHandlerAction action) throws FightException;
    void onTurnStopped(AppendableFightHandlerAction action) throws FightException;

    IFighter getCaster();
    IFighter getTarget();
    SpellTemplate getSpell();
    int getRemainingTurns();

    SpellEffectsEnum getEffect();
    int getValue1();
    int getValue2();
    int getValue3();
    int getChance();
}
