package org.d2j.game.game.fights.buffs;

import org.d2j.common.client.protocol.enums.SpellEffectsEnum;
import org.d2j.game.game.fights.AppendableFightHandlerAction;
import org.d2j.game.game.fights.FightException;
import org.d2j.game.game.fights.IFighter;
import org.d2j.game.game.statistics.CharacteristicType;
import org.d2j.game.model.SpellTemplate;

/**
 * Created by IntelliJ IDEA.
 * User: Blackrush
 * Date: 01/03/12
 * Time: 23:53
 */
public class AddArmorBuff extends AbstractBuff {
    private final CharacteristicType characteristic;
    private final short value;

    public AddArmorBuff(IFighter caster, IFighter target, SpellTemplate spell, int remainingTurns, CharacteristicType characteristic, short value) {
        super(caster, target, spell, remainingTurns);
        this.characteristic = characteristic;
        this.value = value;
    }

    @Override
    public void onAdded(AppendableFightHandlerAction action) throws FightException {
        target.getStatistics().get(characteristic).addGifts(value);
        target.getHandler().notifyRefreshStatistics();
    }

    @Override
    public void onRemoved(AppendableFightHandlerAction action) throws FightException {
        target.getStatistics().get(characteristic).addGifts((short) -value);
        target.getHandler().notifyRefreshStatistics();
    }

    @Override
    public void onTurnStarted(AppendableFightHandlerAction action) throws FightException {
        decrementRemainingTurns();
    }

    @Override
    public void onTurnStopped(AppendableFightHandlerAction action) throws FightException {
    }

    @Override
    public SpellEffectsEnum getEffect() {
        return SpellEffectsEnum.AddArmorBis;
    }

    @Override
    public int getValue1() {
        return value;
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
