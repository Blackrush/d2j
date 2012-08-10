package org.d2j.game.game.spells.effects;

import org.d2j.common.client.protocol.enums.ActionTypeEnum;
import org.d2j.common.client.protocol.enums.OrientationEnum;
import org.d2j.common.client.protocol.enums.SpellEffectsEnum;
import org.d2j.common.random.Dice;
import org.d2j.game.game.fights.*;
import org.d2j.game.game.fights.actions.FightMovement;
import org.d2j.game.game.pathfinding.Node;
import org.d2j.game.game.pathfinding.Pathfinding;
import org.d2j.game.game.spells.ISpellLevel;
import org.d2j.game.game.spells.filters.Filter;
import org.d2j.game.game.spells.filters.TargetEffectFilter;

import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

/**
 * User: Blackrush
 * Date: 22/12/11
 * Time: 23:53
 * IDE : IntelliJ IDEA
 */
public class PushEffect extends Effect {
    private static AtomicReference<Random> RANDOM = new AtomicReference<>(new Random(System.currentTimeMillis()));

    public static short getEffect(int value, int step, int level){
        int _loc1 = RANDOM.get().nextInt(9) + 8; // 8 to 17
        double _loc2 = level / 50;
        if (_loc2 < 0.1) _loc2 = 0.1;
        return (short) (Math.floor(_loc1 * _loc2) * (value - step + 1));
    }

    private int value;

    public PushEffect(ISpellLevel infos) {
        super(infos, SpellEffectsEnum.PushBack);
    }

    @Override
    public int getNbTurns() {
        return 0;
    }

    @Override
    public void setNbTurns(int nbTurns) {
    }

    @Override
    public void setFilter(Filter filter) {
        super.setFilter(new TargetEffectFilter(filter));
    }

    @Override
    public void apply(AppendableFightHandlerAction action, final IFighter caster, FightCell targetCell) throws FightException {
        if (!getFilter().filter(caster, targetCell.getCurrentFighter())) return;

        Fight fight = targetCell.getFight();
        final IFighter target = targetCell.getCurrentFighter();
        final OrientationEnum orientation = Pathfinding.getOrientationFromPoints(
                caster.getCurrentCell().getPosition(),
                targetCell.getPosition()
        );

        FightCell cell = target.getCurrentCell();
        for (int i = 0; i < value; ++i){
            FightCell tmpCell = (FightCell) Pathfinding.getCellByOrientation(
                    fight.getMap().getWidth(),
                    fight.getMap().getHeight(),
                    fight.getCells(),
                    new Node(cell.getId(), cell.getPosition()),
                    orientation
            );

            if (tmpCell == null || !tmpCell.isAvailable()){
                final int effect = target.getStatistics().addLife((short) -getEffect(value, i, target.getLevel()));

                action.append(new FightHandlerAction() {
                    @Override
                    public void call(IFightHandler obj) throws FightException {
                        obj.notifyBasicAction(
                                ActionTypeEnum.LIFE_CHANGEMENT,
                                target,
                                (int) target.getId(),
                                effect
                        );
                    }
                });

                break;
            }

            cell = tmpCell;
        }

        FightMovement.move(target, cell);

        action.append(new FightHandlerAction() {
            @Override
            public void call(IFightHandler obj) throws FightException {
                obj.notifySlide(caster, target);
            }
        });
    }

    @Override
    public int getValue1() {
        return value;
    }

    @Override
    public void setValue1(int value1) {
        this.value = value1;
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
