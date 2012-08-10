package org.d2j.game.game.items;

import org.d2j.common.client.protocol.enums.ItemEffectEnum;
import org.d2j.common.client.protocol.type.BaseItemEffectType;
import org.d2j.game.game.statistics.CharacteristicType;
import org.d2j.game.game.statistics.IStatistics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.d2j.common.StringUtils.fromHex;
import static org.d2j.common.StringUtils.toHex;

/**
 * User: Blackrush
 * Date: 24/12/11
 * Time: 12:04
 * IDE : IntelliJ IDEA
 */
public class ItemEffect {
    public static ItemEffect parseItemEffect(String str){
        String[] args = str.split(",");
        return new ItemEffect(
                ItemEffectEnum.valueOf(fromHex(args[0])),
                (short) fromHex(args[1])
        );
    }

    public static Collection<BaseItemEffectType> toBaseItemEffectType(Collection<ItemEffect> effects){
        List<BaseItemEffectType> result = new ArrayList<>(effects.size());
        for (ItemEffect effect : effects){
            result.add(effect.toBaseItemEffectType());
        }
        return result;
    }

    public static Collection<ItemEffect> copy(Collection<ItemEffect> effects){
        List<ItemEffect> result = new ArrayList<>(effects.size());
        for (ItemEffect effect : effects){
            result.add(effect.copy());
        }
        return result;
    }

    private ItemEffectEnum effect;
    private short bonus;

    public ItemEffect(ItemEffectEnum effect, short bonus) {
        this.effect = effect;
        this.bonus = bonus;
    }

    public ItemEffectEnum getEffect() {
        return effect;
    }

    public short getBonus() {
        return bonus;
    }

    public ItemEffect copy(){
        return new ItemEffect(effect, bonus);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ItemEffect that = (ItemEffect) o;

        return !(bonus != that.bonus || effect != that.effect);

    }

    @Override
    public int hashCode() {
        int result = effect != null ? effect.hashCode() : 0;
        result = 31 * result + (int) bonus;
        return result;
    }

    @Override
    public String toString() {
        return toHex(effect.value()) + "," + toHex(bonus);
    }

    public void apply(IStatistics statistics){
        switch (effect) {
            case AddActionPoints:
                statistics.get(CharacteristicType.ActionPoints).addEquipments(bonus);
                break;
            case SubActionPoints:
                statistics.get(CharacteristicType.ActionPoints).addEquipments((short) -bonus);
                break;

            case AddMovementPoints:
                statistics.get(CharacteristicType.MovementPoints).addEquipments(bonus);
                break;

            case SubMovementPoints:
                statistics.get(CharacteristicType.MovementPoints).addEquipments((short) -bonus);
                break;

            case AddInitiative:
                statistics.get(CharacteristicType.Initiative).addEquipments(bonus);
                break;

            case SubInitiative:
                statistics.get(CharacteristicType.Initiative).addEquipments((short) -bonus);
                break;

            case AddProspection:
                statistics.get(CharacteristicType.Prospection).addEquipments(bonus);
                break;

            case SubProspection:
                statistics.get(CharacteristicType.Prospection).addEquipments((short) -bonus);
                break;

            case AddVitality:
                statistics.get(CharacteristicType.Vitality).addEquipments(bonus);
                break;

            case SubVitality:
                statistics.get(CharacteristicType.Vitality).addEquipments((short) -bonus);
                break;

            case AddWisdom:
                statistics.get(CharacteristicType.Wisdom).addEquipments(bonus);
                break;

            case SubWisdom:
                statistics.get(CharacteristicType.Wisdom).addEquipments((short) -bonus);
                break;

            case AddStrength:
                statistics.get(CharacteristicType.Strength).addEquipments(bonus);
                break;

            case SubStrength:
                statistics.get(CharacteristicType.Strength).addEquipments((short) -bonus);
                break;

            case AddIntelligence:
                statistics.get(CharacteristicType.Intelligence).addEquipments(bonus);
                break;

            case SubIntelligence:
                statistics.get(CharacteristicType.Intelligence).addEquipments((short) -bonus);
                break;

            case AddChance:
                statistics.get(CharacteristicType.Chance).addEquipments(bonus);
                break;

            case SubChance:
                statistics.get(CharacteristicType.Chance).addEquipments((short) -bonus);
                break;

            case AddAgility:
                statistics.get(CharacteristicType.Agility).addEquipments(bonus);
                break;

            case SubAgility:
                statistics.get(CharacteristicType.Agility).addEquipments((short) -bonus);
                break;

            case AddDamage:
                statistics.get(CharacteristicType.Damage).addEquipments(bonus);
                break;

            case SubDamage:
                statistics.get(CharacteristicType.Damage).addEquipments((short) -bonus);
                break;

            case AddDamagePercent:
                statistics.get(CharacteristicType.DamagePer).addEquipments(bonus);
                break;

            case AddCriticalHit:
                statistics.get(CharacteristicType.CriticalHit).addEquipments(bonus);
                break;

            case SubCriticalHit:
                statistics.get(CharacteristicType.CriticalHit).addEquipments((short) -bonus);
                break;

            case AddCriticalFailure:
                statistics.get(CharacteristicType.CriticalFailure).addEquipments(bonus);
                break;

            case AddDodgeAP:
                statistics.get(CharacteristicType.DodgeActionPoints).addEquipments(bonus);
                break;

            case SubDodgeAP:
                statistics.get(CharacteristicType.DodgeActionPoints).addEquipments((short) -bonus);
                break;

            case AddDodgeMP:
                statistics.get(CharacteristicType.DodgeMovementPoints).addEquipments(bonus);
                break;

            case SubDodgeMP:
                statistics.get(CharacteristicType.DodgeMovementPoints).addEquipments((short) -bonus);
                break;

            case AddSummons:
                statistics.get(CharacteristicType.Summons).addEquipments(bonus);
                break;

            case AddRangePoints:
                statistics.get(CharacteristicType.RangePoints).addEquipments(bonus);
                break;

            case SubRangePoints:
                statistics.get(CharacteristicType.RangePoints).addEquipments((short) -bonus);
                break;

            case AddPods:
                break;

            case SubPods:
                break;

            case AddHealPoints:
                statistics.get(CharacteristicType.HealPoints).addEquipments(bonus);
                break;

            case SubHealPoints:
                statistics.get(CharacteristicType.HealPoints).addEquipments((short) -bonus);
                break;

            case MultiplyDamage:
                break;

            case SubDamageMagic:
                break;

            case SubDamagePhysic:
                break;

            case AddReduceDamagePourcentWater:
                break;

            case AddReduceDamagePourcentEarth:
                break;

            case AddReduceDamagePourcentWind:
                break;

            case AddReduceDamagePourcentFire:
                break;

            case AddReduceDamagePourcentNeutral:
                break;

            case AddReduceDamagePourcentPvPWater:
                break;

            case AddReduceDamagePourcentPvPEarth:
                break;

            case AddReduceDamagePourcentPvPWind:
                break;

            case AddReduceDamagePourcentPvPFire:
                break;

            case AddReduceDamagePourcentPvpNeutral:
                break;

            case AddReduceDamageWater:
                break;

            case AddReduceDamageEarth:
                break;

            case AddReduceDamageWind:
                break;

            case AddReduceDamageFire:
                break;

            case AddReduceDamageNeutral:
                break;

            case AddReduceDamagePvPWater:
                break;

            case AddReduceDamagePvPEarth:
                break;

            case AddReduceDamagePvPWind:
                break;

            case AddReduceDamagePvPFire:
                break;

            case AddReduceDamagePvPNeutral:
                break;

            case SubReduceDamagePourcentWater:
                break;

            case SubReduceDamagePourcentEarth:
                break;

            case SubReduceDamagePourcentWind:
                break;

            case SubReduceDamagePourcentFire:
                break;

            case SubReduceDamagePourcentNeutral:
                break;

            case SubReduceDamagePourcentPvPWater:
                break;

            case SubReduceDamagePourcentPvPEarth:
                break;

            case SubReduceDamagePourcentPvPWind:
                break;

            case SubReduceDamagePourcentPvPFire:
                break;

            case SubReduceDamagePourcentPvpNeutral:
                break;

            case SubReduceDamageWater:
                break;

            case SubReduceDamageEarth:
                break;

            case SubReduceDamageWind:
                break;

            case SubReduceDamageFire:
                break;

            case SubReduceDamageNeutral:
                break;
        }
    }

    public BaseItemEffectType toBaseItemEffectType(){
        return new BaseItemEffectType(
                effect,
                bonus
        );
    }
}
