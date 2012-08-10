package org.d2j.game.model;

import org.d2j.common.client.protocol.enums.ItemTypeEnum;
import org.d2j.game.game.items.ItemEffectTemplate;
import org.d2j.game.game.spells.effects.Effect;
import org.d2j.game.game.spells.zones.SingleCell;

import java.util.Collection;

/**
 * User: Blackrush
 * Date: 23/12/11
 * Time: 23:27
 * IDE : IntelliJ IDEA
 */
public class WeaponItemTemplate extends ItemTemplate {
    private boolean twoHands, ethereal;
    private short criticalBonus, cost, minRange, maxRange, criticalRate, criticalFailureRate;
    private Collection<Effect> weaponEffects, weaponEffectsCritic;

    public WeaponItemTemplate(int id, String name, ItemSetTemplate itemSet, ItemTypeEnum type, short level, int weight, boolean forgemageable, int price, String conditions, Collection<ItemEffectTemplate> stats, boolean twoHands, boolean ethereal, short criticalBonus, short cost, short minRange, short maxRange, short criticalRate, short criticalFailureRate, Collection<Effect> weaponEffects, Collection<Effect> weaponEffectsCritic) {
        super(id, name, itemSet, type, level, weight, forgemageable, price, conditions, stats);
        this.twoHands = twoHands;
        this.ethereal = ethereal;
        this.criticalBonus = criticalBonus;
        this.cost = cost;
        this.minRange = minRange;
        this.maxRange = maxRange;
        this.criticalRate = criticalRate;
        this.criticalFailureRate = criticalFailureRate;
        this.weaponEffects = weaponEffects;
        this.weaponEffectsCritic = weaponEffectsCritic;
    }

    @Override
    public boolean isWeapon() {
        return true;
    }

    public boolean isTwoHands() {
        return twoHands;
    }

    public boolean isEthereal() {
        return ethereal;
    }

    public short getCriticalBonus() {
        return criticalBonus;
    }

    public short getCost() {
        return cost;
    }

    public short getMinRange() {
        return minRange;
    }

    public short getMaxRange() {
        return maxRange;
    }

    public short getCriticalRate() {
        return criticalRate;
    }

    public short getCriticalFailureRate() {
        return criticalFailureRate;
    }

    @Override
    public WeaponItem generate() {
        return new WeaponItem(
                this,
                ItemEffectTemplate.generate(getStats()),
                weaponEffects,
                new SingleCell(),
                weaponEffectsCritic,
                new SingleCell()
        );
    }
}
