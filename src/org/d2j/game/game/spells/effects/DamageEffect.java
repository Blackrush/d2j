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
 * Date: 16/12/11
 * Time: 17:31
 * IDE : IntelliJ IDEA
 */
public class DamageEffect extends Effect {
    public static int getEffect(CharacteristicType characteristic, IStatistics statistics, Dice dice){
        int base = dice.roll(),
            charac = statistics.get(characteristic).getSafeTotal(),
            domPercent = statistics.get(CharacteristicType.DamagePer).getSafeTotal(),
            dom = statistics.get(CharacteristicType.Damage).getSafeTotal();

        return base * (100 + charac + domPercent) / 100 + dom;
    }

    public static int getResistance(SpellEffectsEnum effect, IStatistics statistics){
        int resNeutral = statistics.get(CharacteristicType.ResistanceNeutral).getTotal(),
            res = effect == SpellEffectsEnum.DamageNeutral ? 0 : statistics.get(CharacteristicType.getResistanceBySpellEffect(effect)).getTotal(),
            charac = statistics.get(CharacteristicType.getTypeBySpellEffect(effect)).getSafeTotal(),
            characInt = statistics.get(CharacteristicType.Intelligence).getSafeTotal();

        return (resNeutral + res) * Math.max(1 + charac / 100, 1 + charac / 200 + characInt / 200);
    }

    public static int applyResistances(SpellEffectsEnum effect, int damage, final IFighter caster, final IFighter target, AppendableFightHandlerAction action){
        final int res = getResistance(effect, target.getStatistics());
        if (res > 0){
            damage -= res;
            if (damage < 0) damage = 0;


            action.append(new FightHandlerAction() {
                @Override
                public void call(IFightHandler obj) throws FightException {
                    obj.notifyBasicAction(
                            ActionTypeEnum.BLOCKED_DAMAGE,
                            caster,
                            (int)target.getId(),
                            res
                    );
                }
            });
        }
        return damage;
    }

    private final CharacteristicType characteristic;

    private Dice dice;

    public DamageEffect(ISpellLevel infos, SpellEffectsEnum effect, CharacteristicType characteristic) {
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
    public void apply(AppendableFightHandlerAction action, final IFighter caster, final FightCell targetCell) throws FightException {
        if (!getFilter().filter(caster, targetCell.getCurrentFighter())) return;

        if (infos.getTemplate().getSprite() >= 0){ //fixme
            caster.getBuffs().tryRemove(SpellEffectsEnum.Invisible);
        }

        final IFighter target = targetCell.getCurrentFighter();

        final int damage = target.getStatistics().addLife((short) -applyResistances(
                this.effect,
                getEffect(
                        characteristic,
                        caster.getStatistics(),
                        dice
                ),
                caster,
                target,
                action
        ));

        action.append(new FightHandlerAction() {
            @Override
            public void call(IFightHandler obj) throws FightException {
                obj.notifyBasicAction(
                        ActionTypeEnum.LIFE_CHANGEMENT,
                        caster,
                        (int)target.getId(),
                        damage
                );
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
}
