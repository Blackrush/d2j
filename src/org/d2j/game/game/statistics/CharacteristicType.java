package org.d2j.game.game.statistics;

import org.d2j.common.client.protocol.enums.ItemEffectEnum;
import org.d2j.common.client.protocol.enums.SpellEffectsEnum;

import java.util.HashMap;

/**
 * User: Blackrush
 * Date: 12/11/11
 * Time: 10:45
 * IDE : IntelliJ IDEA
 */
public enum CharacteristicType {
    Prospection,
    Initiative,
    ActionPoints,
    MovementPoints,
    Strength,
    Vitality,
    Wisdom,
    Chance,
    Agility,
    Intelligence,
    RangePoints,
    Summons,
    Damage,
    PhysicalDamage,
    WeaponControl,
    DamagePer,
    HealPoints,
    TrapDamage,
    TrapDamagePer,
    DamageReturn,
    CriticalHit,
    CriticalFailure,

    DodgeActionPoints,
    DodgeMovementPoints,

    ResistanceNeutral,
    ResistancePercentNeutral,
    ResistancePvpNeutral,
    ResistancePercentPvpNeutral,

    ResistanceEarth,
    ResistancePercentEarth,
    ResistancePvpEarth,
    ResistancePercentPvpEarth,

    ResistanceWater,
    ResistancePercentWater,
    ResistancePvpWater,
    ResistancePercentPvpWater,

    ResistanceWind,
    ResistancePercentWind,
    ResistancePvpWind,
    ResistancePercentPvpWind,

    ResistanceFire,
    ResistancePercentFire,
    ResistancePvpFire,
    ResistancePercentPvpFire;

    private static HashMap<Integer, CharacteristicType> values = new HashMap<>();
    static{
        for (CharacteristicType value : values()){
            values.put(value.ordinal(), value);
        }
    }
    public static CharacteristicType valueOf(int ordinal){
        return values.get(ordinal);
    }
    public static CharacteristicType fromItemEffect(ItemEffectEnum effect){
        switch (effect) {
            case AddActionPoints:
            case SubActionPoints:
                return CharacteristicType.ActionPoints;

            case AddMovementPoints:
            case SubMovementPoints:
                return CharacteristicType.MovementPoints;

            case AddInitiative:
            case SubInitiative:
                return CharacteristicType.Initiative;

            case AddProspection:
            case SubProspection:
                return CharacteristicType.Prospection;

            case AddVitality:
            case SubVitality:
                return CharacteristicType.Vitality;

            case AddWisdom:
            case SubWisdom:
                return CharacteristicType.Wisdom;

            case AddStrength:
            case SubStrength:
            case StolenEarth:
            case InflictDamageEarth:
            case StolenNeutral:
            case InflictDamageNeutral:
                return CharacteristicType.Strength;

            case AddIntelligence:
            case SubIntelligence:
            case StolenFire:
            case InflictDamageFire:
                return CharacteristicType.Intelligence;

            case AddChance:
            case SubChance:
            case StolenWater:
            case InflictDamageWater:
                return CharacteristicType.Chance;

            case AddAgility:
            case SubAgility:
            case StolenWind:
            case InflictDamageWind:
                return CharacteristicType.Agility;

            case AddDamage:
            case SubDamage:
                return CharacteristicType.Damage;

            case AddDamagePercent:
                return CharacteristicType.DamagePer;

            case AddCriticalHit:
            case SubCriticalHit:
                return CharacteristicType.CriticalHit;

            case AddCriticalFailure:
                return CharacteristicType.CriticalFailure;

            case AddDodgeAP:
            case SubDodgeAP:
                return CharacteristicType.DodgeActionPoints;

            case AddDodgeMP:
            case SubDodgeMP:
                return CharacteristicType.DodgeMovementPoints;

            case AddSummons:
                return CharacteristicType.Summons;

            case AddRangePoints:
            case SubRangePoints:
                return CharacteristicType.RangePoints;

            case AddHealPoints:
            case SubHealPoints:
                return CharacteristicType.HealPoints;

            default: return null;
        }
    }

    public static CharacteristicType getResistanceBySpellEffect(SpellEffectsEnum effect){
        switch (effect){
            case DamageNeutral:
            case StealNeutral:
                return ResistanceNeutral;

            case DamageEarth:
            case StealEarth:
                return ResistanceEarth;

            case DamageFire:
            case StealFire:
                return ResistanceFire;

            case DamageWind:
            case StealWind:
                return ResistanceWind;

            case DamageWater:
            case StealWater:
                return ResistanceWater;

            default: return null;
        }
    }

    public static CharacteristicType getTypeBySpellEffect(SpellEffectsEnum effect){
        switch (effect){
            case DamageNeutral:
            case StealNeutral:
            case DamageEarth:
            case StealEarth:
                return Strength;

            case DamageFire:
            case StealFire:
                return Intelligence;

            case DamageWind:
            case StealWind:
                return Agility;

            case DamageWater:
            case StealWater:
                return Chance;

            default: return null;
        }
    }
}
