package org.d2j.game.game.spells.effects;

import org.d2j.common.client.protocol.enums.SpellEffectsEnum;
import org.d2j.common.random.Dice;
import org.d2j.game.game.fights.AppendableFightHandlerAction;
import org.d2j.game.game.fights.FightCell;
import org.d2j.game.game.fights.FightException;
import org.d2j.game.game.fights.IFighter;
import org.d2j.game.game.fights.buffs.AddArmorBuff;
import org.d2j.game.game.fights.buffs.Buff;
import org.d2j.game.game.fights.buffs.BuffMaker;
import org.d2j.game.game.spells.ISpellLevel;
import org.d2j.game.game.statistics.CharacteristicType;

/**
 * Created by IntelliJ IDEA.
 * User: Blackrush
 * Date: 01/03/12
 * Time: 23:49
 */
public class AddArmorEffect extends Effect implements BuffMaker {
    private final CharacteristicType characteristic;

    private Dice dice;

    public AddArmorEffect(ISpellLevel infos) {
        super(infos, SpellEffectsEnum.AddArmorBis);

        switch (infos.getTemplate().getId()){
            case 1:
                characteristic = CharacteristicType.ResistanceFire;
                break;

            case 6:
                characteristic = CharacteristicType.ResistanceEarth;
                break;

            case 14:
                characteristic = CharacteristicType.ResistanceWind;
                break;

            case 18:
                characteristic = CharacteristicType.ResistanceWater;
                break;

            default:
                characteristic = CharacteristicType.ResistanceNeutral;
                break;
        }
    }

    @Override
    public Buff make(IFighter caster, IFighter target) {
        return new AddArmorBuff(
                caster,
                target,
                infos.getTemplate(),
                getNbTurns(),
                characteristic,
                (short) dice.roll()
        );
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
        return dice;
    }

    @Override
    public void setDice(Dice dice) {
        this.dice = dice;
    }
}
