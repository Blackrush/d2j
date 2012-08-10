package org.d2j.game.game.fights.buffs;

import org.d2j.common.client.protocol.enums.SpellEffectsEnum;
import org.d2j.game.game.fights.*;
import org.d2j.game.model.SpellTemplate;

/**
 * Created by IntelliJ IDEA.
 * User: Blackrush
 * Date: 06/03/12
 * Time: 00:05
 */
public class InvisibleBuff extends AbstractBuff {
    public InvisibleBuff(IFighter caster, IFighter target, SpellTemplate spell, int remainingTurns) {
        super(caster, target, spell, remainingTurns);
    }

    @Override
    public void onAdded(AppendableFightHandlerAction action) throws FightException {
        action.append(new FightHandlerAction() {
            @Override
            public void call(IFightHandler obj) throws FightException {
                obj.notifyInvisible(caster, target, true, getRemainingTurns());
            }
        });
    }

    @Override
    public void onRemoved(AppendableFightHandlerAction action) throws FightException {
        action.append(new FightHandlerAction() {
            @Override
            public void call(IFightHandler obj) throws FightException {
                obj.notifyInvisible(caster, target, false, 0);
            }
        });
    }

    @Override
    public void onTurnStarted(AppendableFightHandlerAction action) throws FightException {

    }

    @Override
    public void onTurnStopped(AppendableFightHandlerAction action) throws FightException {
        decrementRemainingTurns();
    }

    @Override
    public SpellEffectsEnum getEffect() {
        return SpellEffectsEnum.Invisible;
    }

    @Override
    public int getValue1() {
        return 0;
    }

    @Override
    public int getValue2() {
        return 0;
    }

    @Override
    public int getValue3() {
        return 0;
    }

    @Override
    public int getChance() {
        return 0;
    }
}
