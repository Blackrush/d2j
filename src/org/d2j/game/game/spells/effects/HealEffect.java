package org.d2j.game.game.spells.effects;

import org.d2j.common.client.protocol.enums.ActionTypeEnum;
import org.d2j.common.client.protocol.enums.SpellEffectsEnum;
import org.d2j.common.random.Dice;
import org.d2j.game.game.fights.*;
import org.d2j.game.game.spells.ISpellLevel;
import org.d2j.game.game.spells.filters.Filter;
import org.d2j.game.game.spells.filters.TargetEffectFilter;
import org.d2j.game.game.statistics.CharacteristicType;
import org.d2j.game.game.statistics.IStatistics;

/**
 * User: Blackrush
 * Date: 19/12/11
 * Time: 19:04
 * IDE : IntelliJ IDEA
 */
public class HealEffect extends Effect {
    public static int getEffect(Dice dice, IStatistics statistics){
        return dice.roll() *
              (100 + statistics.get(CharacteristicType.Intelligence).getSafeTotal()) / 100 +
               statistics.get(CharacteristicType.HealPoints).getSafeTotal();
    }

    private Dice dice;

    public HealEffect(ISpellLevel infos) {
        super(infos, SpellEffectsEnum.Heal);
    }

    @Override
    public void setFilter(Filter filter) {
        super.setFilter(new TargetEffectFilter(filter));
    }

    @Override
    public void apply(AppendableFightHandlerAction action, final IFighter caster, final FightCell targetCell) throws FightException {
        if (!getFilter().filter(caster, targetCell.getCurrentFighter())) return;

        final int bonus = targetCell.getCurrentFighter()
                                    .getStatistics()
                                    .addLife((short) getEffect(dice, caster.getStatistics()));

        action.append(new FightHandlerAction() {
            @Override
            public void call(IFightHandler obj) throws FightException {
                obj.notifyBasicAction(
                        ActionTypeEnum.LIFE_CHANGEMENT,
                        caster,
                        (int) targetCell.getCurrentFighter().getId(),
                        bonus
                );
            }
        });
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
