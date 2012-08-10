package org.d2j.game.game.fights.buffs;

import org.d2j.common.client.protocol.enums.SpellEffectsEnum;
import org.d2j.game.game.fights.AppendableFightHandlerAction;
import org.d2j.game.game.fights.FightException;
import org.d2j.game.game.fights.IFighter;
import org.d2j.game.model.SpellTemplate;

/**
 * Created by IntelliJ IDEA.
 * User: Blackrush
 * Date: 09/03/12
 * Time: 11:58
 */
public class PunishmentBuff extends AbstractBuff {
    private final SpellEffectsEnum effect;
    private final int max;

    private int actual;

    public PunishmentBuff(IFighter caster, IFighter target, SpellTemplate spell, int remainingTurns, SpellEffectsEnum effect, int max) {
        super(caster, target, spell, remainingTurns);
        this.effect = effect;
        this.max = max;
    }

    @Override
    public void onAdded(AppendableFightHandlerAction action) throws FightException {
    }

    @Override
    public void onRemoved(AppendableFightHandlerAction action) throws FightException {
    }

    @Override
    public void onTurnStarted(AppendableFightHandlerAction action) throws FightException {
    }

    @Override
    public void onTurnStopped(AppendableFightHandlerAction action) throws FightException {
    }

    @Override
    public SpellEffectsEnum getEffect() {
        return effect;
    }

    @Override
    public int getValue1() {
        return actual;
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
