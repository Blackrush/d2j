package org.d2j.game.game.fights.buffs;

import org.d2j.common.client.protocol.enums.SpellEffectsEnum;
import org.d2j.game.game.fights.AppendableFightHandlerAction;
import org.d2j.game.game.fights.FightException;
import org.d2j.game.game.fights.IFighter;
import org.d2j.game.game.spells.effects.Effect;
import org.d2j.game.model.SpellTemplate;

/**
 * Created by IntelliJ IDEA.
 * User: Blackrush
 * Date: 01/03/12
 * Time: 20:41
 */
public class EffectContainerBuff extends AbstractBuff {
    private final Effect effect;

    public EffectContainerBuff(IFighter caster, IFighter target, SpellTemplate spell, Effect effect) {
        super(caster, target, spell, effect.getNbTurns());
        this.effect = effect;
    }

    @Override
    public void onAdded(AppendableFightHandlerAction action) throws FightException {
    }

    @Override
    public void onRemoved(AppendableFightHandlerAction action) throws FightException {
    }

    @Override
    public void onTurnStarted(AppendableFightHandlerAction action) throws FightException {
        effect.apply(action, caster, target.getCurrentCell());
        decrementRemainingTurns();
    }

    @Override
    public void onTurnStopped(AppendableFightHandlerAction action) throws FightException {
    }

    @Override
    public SpellEffectsEnum getEffect() {
        return effect.getEffect();
    }

    @Override
    public int getValue1() {
        return effect.getValue1();
    }

    @Override
    public int getValue2() {
        return effect.getValue2();
    }

    @Override
    public int getValue3() {
        return effect.getValue3();
    }

    @Override
    public int getChance() {
        return effect.getChance();
    }
}
