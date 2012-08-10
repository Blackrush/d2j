package org.d2j.game.game.spells.effects;

import org.d2j.common.client.protocol.enums.SpellEffectsEnum;
import org.d2j.common.random.Dice;
import org.d2j.game.game.fights.*;
import org.d2j.game.game.spells.ISpellLevel;
import org.d2j.game.game.spells.filters.Filter;
import org.d2j.game.game.spells.filters.TargetEffectFilter;

/**
 * Created by IntelliJ IDEA.
 * User: Blackrush
 * Date: 05/03/12
 * Time: 10:12
 */
public class TransposeEffect extends Effect {
    public TransposeEffect(ISpellLevel infos) {
        super(infos, SpellEffectsEnum.Transpose);
    }

    @Override
    public void setFilter(Filter filter) {
        super.setFilter(new TargetEffectFilter(filter));
    }

    @Override
    public void apply(AppendableFightHandlerAction action, final IFighter caster, FightCell targetCell) throws FightException {
        if (!getFilter().filter(caster, targetCell.getCurrentFighter())) return;

        final IFighter target = targetCell.getCurrentFighter();

        target.setCurrentCell(caster.getCurrentCell());
        caster.setCurrentCell(targetCell);

        action.append(new FightHandlerAction() {
            @Override
            public void call(IFightHandler obj) throws FightException {
                obj.notifyTransposition(caster, target);
            }
        });
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

    @Override
    public Dice getDice() {
        return null;
    }

    @Override
    public void setDice(Dice dice) {
    }
}
