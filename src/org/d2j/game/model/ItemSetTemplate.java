package org.d2j.game.model;

import org.d2j.game.game.items.ItemEffect;
import org.d2j.game.game.statistics.IStatistics;
import org.d2j.utils.database.entity.IBaseEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * User: Blackrush
 * Date: 28/12/11
 * Time: 11:24
 * IDE : IntelliJ IDEA
 */
public class ItemSetTemplate implements IBaseEntity<Short> {
    public static Collection<Item> ofItemSet(ItemSetTemplate itemSet, Collection<Item> items){
        List<Item> result = new ArrayList<>();
        for (Item item : items){
            if (item.getTemplate().getItemSet() == itemSet){
                result.add(item);
            }
        }
        return result;
    }

    private short id;
    private String name;
    private List<ItemTemplate> items;
    private List<Collection<ItemEffect>> effects;

    public ItemSetTemplate(short id, String name, List<ItemTemplate> items, List<Collection<ItemEffect>> effects) {
        this.id = id;
        this.name = name;
        this.items = items;
        this.effects = effects;
    }

    @Override
    public Short getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<ItemTemplate> getItems() {
        return items;
    }

    public List<Collection<ItemEffect>> getEffects() {
        return effects;
    }

    public Collection<ItemEffect> apply(int equiped, IStatistics statistics){
        int index = equiped - 2;
        if (index < 0 || index >= effects.size()) return null;

        for (ItemEffect effect : effects.get(index)){
            effect.apply(statistics);
        }
        return effects.get(index);
    }
}
