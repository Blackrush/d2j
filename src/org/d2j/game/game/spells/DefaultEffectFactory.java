package org.d2j.game.game.spells;

import org.d2j.common.client.protocol.enums.SpellEffectsEnum;
import org.d2j.common.random.Dice;
import org.d2j.game.game.spells.effects.*;
import org.d2j.game.game.spells.filters.TargetFilter;
import org.d2j.game.game.spells.zones.Zones;
import org.d2j.game.game.statistics.CharacteristicType;
import org.d2j.game.model.SpellTemplate;
import org.d2j.utils.Reference;
import org.d2j.utils.SimpleReference;
import org.d2j.utils.database.repository.IBaseEntityRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * User: Blackrush
 * Date: 08/12/11
 * Time: 20:26
 * IDE : IntelliJ IDEA
 */
public class DefaultEffectFactory implements IEffectFactory {
    private static final DefaultEffectFactory self = new DefaultEffectFactory();
    public static IEffectFactory getInstance(){
        return self;
    }

    private HashMap<SpellEffectsEnum, EffectMaker> effects = new HashMap<>();

    private DefaultEffectFactory(){}

    @Override
    public ISpellLevel parse(SpellTemplate template, String str) {
        if (str.equals("-1") || str.isEmpty()) return null;

        String[] stats = str.split(",");

        String zone = stats[15].trim();

        SpellLevel level = new SpellLevel(
                template,
                stats[2].isEmpty() ? 6 : Short.parseShort(stats[2].trim()),
                Short.parseShort  (stats[3].trim()),
                Short.parseShort  (stats[4].trim()),
                Short.parseShort  (stats[5].trim()),
                Short.parseShort  (stats[6].trim()),
                stats[7] .contains("true"),
                stats[8] .contains("true"),
                stats[9] .contains("true"),
                stats[10].contains("true"),
                Integer.parseInt  (stats[12].trim()),
                Integer.parseInt  (stats[13].trim())
        );

        if (stats[1].equals("-1")){ // there are not critical effects
            level.setEffects(parseEffects(level, stats[0], zone, new SimpleReference<Integer>()));
            level.setCriticalEffects(new ArrayList<Effect>());
        }
        else{
            Reference<Integer> indexZone = new SimpleReference<>();
            level.setEffects(parseEffects(level, stats[0], zone, indexZone));
            level.setCriticalEffects(parseEffects(level, stats[1], zone, indexZone));
        }

        return level;
    }

    @Override
    public void load(final IBaseEntityRepository<SpellTemplate, Integer> spells) {
        effects.put(SpellEffectsEnum.Teleport, new EffectMaker() {
            @Override
            public Effect make(ISpellLevel infos) {
                return new TeleportationEffect(infos);
            }
        });

        effects.put(SpellEffectsEnum.DamageEarth, new EffectMaker() {
            @Override
            public Effect make(ISpellLevel infos) {
                return new DamageEffect(infos, SpellEffectsEnum.DamageEarth, CharacteristicType.Strength);
            }
        });
        effects.put(SpellEffectsEnum.DamageFire, new EffectMaker() {
            @Override
            public Effect make(ISpellLevel infos) {
                return new DamageEffect(infos, SpellEffectsEnum.DamageFire, CharacteristicType.Intelligence);
            }
        });
        effects.put(SpellEffectsEnum.DamageWater, new EffectMaker() {
            @Override
            public Effect make(ISpellLevel infos) {
                return new DamageEffect(infos, SpellEffectsEnum.DamageWater, CharacteristicType.Chance);
            }
        });
        effects.put(SpellEffectsEnum.DamageWind, new EffectMaker() {
            @Override
            public Effect make(ISpellLevel infos) {
                return new DamageEffect(infos, SpellEffectsEnum.DamageWind, CharacteristicType.Agility);
            }
        });
        effects.put(SpellEffectsEnum.DamageNeutral, new EffectMaker() {
            @Override
            public Effect make(ISpellLevel infos) {
                return new DamageEffect(infos, SpellEffectsEnum.DamageNeutral, CharacteristicType.Strength);
            }
        });

        effects.put(SpellEffectsEnum.StealEarth, new EffectMaker() {
            @Override
            public Effect make(ISpellLevel infos) {
                return new StealLifeEffect(infos, SpellEffectsEnum.StealEarth, CharacteristicType.Strength);
            }
        });
        effects.put(SpellEffectsEnum.StealFire, new EffectMaker() {
            @Override
            public Effect make(ISpellLevel infos) {
                return new StealLifeEffect(infos, SpellEffectsEnum.StealFire, CharacteristicType.Intelligence);
            }
        });
        effects.put(SpellEffectsEnum.StealWater, new EffectMaker() {
            @Override
            public Effect make(ISpellLevel infos) {
                return new StealLifeEffect(infos, SpellEffectsEnum.StealWater, CharacteristicType.Chance);
            }
        });
        effects.put(SpellEffectsEnum.StealWind, new EffectMaker() {
            @Override
            public Effect make(ISpellLevel infos) {
                return new StealLifeEffect(infos, SpellEffectsEnum.StealWind, CharacteristicType.Agility);
            }
        });
        effects.put(SpellEffectsEnum.StealNeutral, new EffectMaker() {
            @Override
            public Effect make(ISpellLevel infos) {
                return new StealLifeEffect(infos, SpellEffectsEnum.StealNeutral, CharacteristicType.Strength);
            }
        });

        effects.put(SpellEffectsEnum.PushBack, new EffectMaker() {
            @Override
            public Effect make(ISpellLevel infos) {
                return new PushEffect(infos);
            }
        });

        effects.put(SpellEffectsEnum.Heal, new EffectMaker() {
            @Override
            public Effect make(ISpellLevel infos) {
                return new HealEffect(infos);
            }
        });

        effects.put(SpellEffectsEnum.AddArmorBis, new EffectMaker() {
            @Override
            public Effect make(ISpellLevel infos) {
                return new AddArmorEffect(infos);
            }
        });

        effects.put(SpellEffectsEnum.Transpose, new EffectMaker() {
            @Override
            public Effect make(ISpellLevel infos) {
                return new TransposeEffect(infos);
            }
        });

        effects.put(SpellEffectsEnum.ClearBuffs, new EffectMaker() {
            @Override
            public Effect make(ISpellLevel infos) {
                return new ClearBuffsEffect(infos);
            }
        });

        effects.put(SpellEffectsEnum.Invisible, new EffectMaker() {
            @Override
            public Effect make(ISpellLevel infos) {
                return new InvisibleEffect(infos);
            }
        });

        effects.put(SpellEffectsEnum.UseTrap, new EffectMaker() {
            @Override
            public Effect make(ISpellLevel infos) {
                return new UseTrapEffect(infos, spells);
            }
        });

        effects.put(SpellEffectsEnum.UseGlyph, new EffectMaker() {
            @Override
            public Effect make(ISpellLevel infos) {
                return new UseGlyphEffect(infos, spells);
            }
        });

        effects.put(SpellEffectsEnum.AddPunishment, new EffectMaker() {
            @Override
            public Effect make(ISpellLevel infos) {
                return new PunishmentEffect(infos);
            }
        });
    }

    public Effect get(SpellEffectsEnum effect){
        if (effect == null) return null;
        EffectMaker maker = effects.get(effect);
        return maker != null ? maker.make(null) : null;
    }

    private Collection<Effect> parseEffects(ISpellLevel infos, String str, String zone, Reference<Integer> startZone){
        List<Effect> effects = new ArrayList<>();

        int currentZone = startZone.isNull() ? 0 : startZone.get();

        for (String effectStr : str.split("\\|")){
            if (effectStr.isEmpty() || effectStr.equals("-1")){
                continue;
            }

            String[] args = effectStr.split(";");

            SpellEffectsEnum effectId = SpellEffectsEnum.valueOf(Integer.parseInt(args[0]));
            EffectMaker maker = this.effects.get(effectId);
            if (maker != null){
                Effect effect = maker.make(infos);

                effect.setValue1(Integer.parseInt(args[1]));
                effect.setValue2(Integer.parseInt(args[2]));
                effect.setValue3(Integer.parseInt(args[3]));
                if (args.length > 4){
                    effect.setNbTurns(Integer.parseInt(args[4]));
                }
                if (args.length > 5){
                    effect.setChance(Integer.parseInt(args[5]));
                }
                if (args.length > 6){
                    effect.setDice(Dice.parseDice(args[6]));
                }
                if (args.length > 7){
                    effect.setFilter(TargetFilter.parseTargetFilter(Integer.parseInt(args[7])));
                }
                else{
                    effect.setFilter(TargetFilter.newDefault());
                }
                effect.setZone(Zones.parseZone(zone.substring(currentZone * 2, (currentZone + 1) * 2)));

                effects.add(effect);
                ++currentZone;
            }
        }

        startZone.set(currentZone);

        return effects;
    }
}
