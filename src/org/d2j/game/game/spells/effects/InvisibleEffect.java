package org.d2j.game.game.spells.effects;

import org.d2j.common.client.protocol.enums.SpellEffectsEnum;
import org.d2j.common.random.Dice;
import org.d2j.game.game.fights.AppendableFightHandlerAction;
import org.d2j.game.game.fights.FightCell;
import org.d2j.game.game.fights.FightException;
import org.d2j.game.game.fights.IFighter;
import org.d2j.game.game.fights.buffs.Buff;
import org.d2j.game.game.fights.buffs.BuffMaker;
import org.d2j.game.game.fights.buffs.InvisibleBuff;
import org.d2j.game.game.spells.ISpellLevel;

/**
 * Created by IntelliJ IDEA.
 * User: Blackrush
 * Date: 05/03/12
 * Time: 23:58
 */
public class InvisibleEffect extends Effect implements BuffMaker {
    public InvisibleEffect(ISpellLevel infos) {
        super(infos, SpellEffectsEnum.Invisible);
    }

    @Override
    public void apply(AppendableFightHandlerAction action, IFighter caster, FightCell target) throws FightException {
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

    @Override
    public Buff make(IFighter caster, IFighter target) {
        return new InvisibleBuff(
                caster,
                target,
                infos.getTemplate(),
                getNbTurns()
        );
    }
}
