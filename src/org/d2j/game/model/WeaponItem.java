package org.d2j.game.model;

import org.d2j.common.client.protocol.enums.ItemPositionEnum;
import org.d2j.game.game.items.ItemEffect;
import org.d2j.game.game.spells.effects.Effect;
import org.d2j.game.game.spells.zones.Zone;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 14/02/12
 * Time: 23:37
 * To change this template use File | Settings | File Templates.
 */
public class WeaponItem extends Item {
    private WeaponItemTemplate template;
    private Collection<Effect> weaponEffects;
    private Zone zone;
    private Collection<Effect> weaponEffectsCritic;
    private Zone criticalZone;

    public WeaponItem(WeaponItemTemplate template, Collection<ItemEffect> effects, Collection<Effect> weaponEffects, Zone zone, Collection<Effect> weaponEffectsCritic, Zone criticalZone) {
        super(template, effects);
        this.template = template;
        this.weaponEffects = weaponEffects;
        this.zone = zone;
        this.weaponEffectsCritic = weaponEffectsCritic;
        this.criticalZone = criticalZone;
    }

    public WeaponItem(long id, WeaponItemTemplate template, Character owner, Collection<ItemEffect> effects, ItemPositionEnum position, int quantity, Collection<Effect> weaponEffects, Zone zone, Collection<Effect> weaponEffectsCritic, Zone criticalZone) {
        super(id, template, owner, effects, position, quantity);
        this.template = template;
        this.weaponEffects = weaponEffects;
        this.zone = zone;
        this.weaponEffectsCritic = weaponEffectsCritic;
        this.criticalZone = criticalZone;
    }

    @Override
    public WeaponItemTemplate getTemplate() {
        return template;
    }

    public Collection<Effect> getWeaponEffects() {
        return weaponEffects;
    }

    public Zone getZone() {
        return zone;
    }

    public Collection<Effect> getWeaponEffectsCritic() {
        return weaponEffectsCritic;
    }

    public Zone getCriticalZone() {
        return criticalZone;
    }
}
