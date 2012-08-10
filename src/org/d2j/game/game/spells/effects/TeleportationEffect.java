package org.d2j.game.game.spells.effects;

import org.d2j.common.client.protocol.enums.SpellEffectsEnum;
import org.d2j.common.random.Dice;
import org.d2j.game.game.fights.*;
import org.d2j.game.game.fights.actions.FightMovement;
import org.d2j.game.game.spells.ISpellLevel;
import org.d2j.game.game.spells.SpellException;

/**
 * User: Blackrush
 * Date: 14/12/11
 * Time: 16:34
 * IDE : IntelliJ IDEA
 */
public class TeleportationEffect extends Effect {
    public TeleportationEffect(ISpellLevel infos) {
        super(infos, SpellEffectsEnum.Teleport);
    }

    @Override
    public int getNbTurns() {
        return 0;
    }

    @Override
    public void setNbTurns(int nbTurns) {
    }

    @Override
    public void apply(AppendableFightHandlerAction action, final IFighter caster, final FightCell target) throws FightException {
        if (caster.getCurrentCell() == target){
            throw new SpellException("Invalid request: player already on this cell.");
        }
        else if (!target.isAvailable()){
            throw new SpellException("Invalid request: selected cell isn't available or walkable.");
        }
        FightMovement.move(caster, target);

        action.append(new FightHandlerAction() {
            @Override
            public void call(IFightHandler obj) throws FightException {
                obj.notifyTeleportation(caster, target);
            }
        });

        target.getFight().applyCellEffects(caster, target);
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
