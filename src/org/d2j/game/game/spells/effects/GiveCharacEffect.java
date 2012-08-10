package org.d2j.game.game.spells.effects;

import org.d2j.common.client.protocol.enums.SpellEffectsEnum;
import org.d2j.common.random.Dice;
import org.d2j.game.game.fights.AppendableFightHandlerAction;
import org.d2j.game.game.fights.FightCell;
import org.d2j.game.game.fights.FightException;
import org.d2j.game.game.fights.IFighter;
import org.d2j.game.game.spells.ISpellLevel;
import org.d2j.game.game.spells.filters.Filter;
import org.d2j.game.game.spells.filters.TargetEffectFilter;
import org.d2j.game.game.statistics.CharacteristicType;

/**
 * User: Blackrush
 * Date: 19/12/11
 * Time: 18:50
 * IDE : IntelliJ IDEA
 */
public class GiveCharacEffect extends Effect {
    private final CharacteristicType characteristic;
    private Dice dice;

    protected GiveCharacEffect(ISpellLevel infos, SpellEffectsEnum effect, CharacteristicType characteristic) {
        super(infos, effect);
        this.characteristic = characteristic;
    }

    @Override
    public Dice getDice() {
        return dice;
    }

    @Override
    public void setDice(Dice dice) {
        this.dice = dice;
    }

    @Override
    public void setFilter(Filter filter) {
        super.setFilter(new TargetEffectFilter(filter));
    }

    @Override
    public void apply(AppendableFightHandlerAction action, IFighter caster, FightCell target) throws FightException {
        if (!getFilter().filter(caster, target.getCurrentFighter())) return;

        int bonus = dice.roll();
        target.getCurrentFighter().getStatistics().get(characteristic).addContext((short) bonus);
        target.getCurrentFighter().getHandler().notifyRefreshStatistics();
    }

    @Override
    public int getValue1() {
        return 0;
    }

    @Override
    public void setValue1(int value1) {
    }

    @Override
    public int getValue2() {
        return 0;
    }

    @Override
    public void setValue2(int value2) {
    }

    @Override
    public int getValue3() {
        return 0;
    }

    @Override
    public void setValue3(int value3) {
    }

    @Override
    public int getChance() {
        return 0;
    }

    @Override
    public void setChance(int chance) {
    }
}
