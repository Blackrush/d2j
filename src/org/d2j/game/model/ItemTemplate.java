package org.d2j.game.model;

import org.d2j.common.client.protocol.enums.ItemTypeEnum;
import org.d2j.common.client.protocol.type.BaseItemTemplateType;
import org.d2j.game.game.items.ItemEffectTemplate;
import org.d2j.game.game.statistics.CharacteristicType;
import org.d2j.game.game.statistics.IStatistics;
import org.d2j.utils.database.entity.IBaseEntity;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * User: Blackrush
 * Date: 23/12/11
 * Time: 19:54
 * IDE : IntelliJ IDEA
 */
public class ItemTemplate implements IBaseEntity<Integer> {
    public static int[] toInt(Collection<ItemTemplate> items) {
        int[] result = new int[items.size()];
        int i = 0;
        for (ItemTemplate tpl : items){
            result[i] = tpl.getId();
            ++i;
        }
        return result;
    }

    public static Collection<BaseItemTemplateType> toBaseItemTemplateType(Collection<ItemTemplate> templates){
        List<BaseItemTemplateType> result = new ArrayList<>(templates.size());
        for (ItemTemplate template : templates){
            result.add(template.toBaseItemTemplateType());
        }
        return result;
    }

    public static boolean isValid(ScriptEngine engine, Character character, ItemTemplate template) throws ScriptException {
        if (template.conditions == null || template.conditions.isEmpty()) return true;

        engine.put("CI", character.getStatistics().get(CharacteristicType.Intelligence).getTotal());
        engine.put("CV", character.getStatistics().get(CharacteristicType.Vitality).getTotal());
        engine.put("CA", character.getStatistics().get(CharacteristicType.Agility).getTotal());
        engine.put("CW", character.getStatistics().get(CharacteristicType.Wisdom).getTotal());
        engine.put("CC", character.getStatistics().get(CharacteristicType.Chance).getTotal());
        engine.put("CS", character.getStatistics().get(CharacteristicType.Strength).getTotal());

        engine.put("Ci", character.getStatistics().get(CharacteristicType.Intelligence).getBase());
        engine.put("Cv", character.getStatistics().get(CharacteristicType.Vitality).getBase());
        engine.put("Ca", character.getStatistics().get(CharacteristicType.Agility).getBase());
        engine.put("Cw", character.getStatistics().get(CharacteristicType.Wisdom).getBase());
        engine.put("Cc", character.getStatistics().get(CharacteristicType.Chance).getBase());
        engine.put("Cs", character.getStatistics().get(CharacteristicType.Strength).getBase());

        engine.put("Ps", 0); // alignment id
        engine.put("Pa", 0); // alignement level
        engine.put("PP", 0); // alignement level
        engine.put("PL", character.getExperience().getLevel());
        engine.put("PK", character.getBag().getKamas());
        engine.put("PG", character.getBreed().getId());
        engine.put("PS", character.getGender() ? "1" : "0");
        engine.put("PZ", character.getOwner().isSubscriber());
        engine.put("PX", character.getOwner().getRights().value());
        engine.put("PW", character.getStatistics().getMaxPods());
        engine.put("PB", 0); // subarea id
        engine.put("PR", 0); // is married
        engine.put("SI", character.getCurrentMap().getId());
        engine.put("MiS", character.getId()); // soulstone

        return (Boolean)engine.eval(template.conditions);
    }

    private int id;
    private String name;
    private ItemSetTemplate itemSet;
    private ItemTypeEnum type;
    private short level;
    private int weight;
    private boolean forgemageable;
    private int price;
    private String conditions;
    private Collection<ItemEffectTemplate> stats;

    public ItemTemplate(int id, String name, ItemSetTemplate itemSet, ItemTypeEnum type, short level, int weight, boolean forgemageable, int price, String conditions, Collection<ItemEffectTemplate> stats) {
        this.id = id;
        this.name = name;
        this.itemSet = itemSet;
        this.type = type;
        this.level = level;
        this.weight = weight;
        this.forgemageable = forgemageable;
        this.price = price;
        this.conditions = conditions;
        this.stats = stats;
    }

    public boolean isWeapon(){
        return false;
    }

    public boolean isUsable(){
        return false;
    }

    public boolean isTargetable(){
        return false;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ItemSetTemplate getItemSet() {
        return itemSet;
    }

    public ItemTypeEnum getType() {
        return type;
    }

    public short getLevel() {
        return level;
    }

    public int getWeight() {
        return weight;
    }

    public boolean isForgemageable() {
        return forgemageable;
    }

    public int getPrice() {
        return price;
    }

    public Collection<ItemEffectTemplate> getStats() {
        return stats;
    }

    public Item generate(){
        return new Item(
                this,
                ItemEffectTemplate.generate(stats)
        );
    }

    public BaseItemTemplateType toBaseItemTemplateType(){
        return new BaseItemTemplateType(
                id,
                ItemEffectTemplate.toBaseItemTemplateEffectType(stats)
        );
    }
}
