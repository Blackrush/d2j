package org.d2j.game.model;

import org.d2j.common.client.protocol.enums.ItemPositionEnum;
import org.d2j.common.client.protocol.type.BaseItemType;
import org.d2j.game.game.items.ItemEffect;
import org.d2j.game.game.statistics.IStatistics;
import org.d2j.utils.Comparator;
import org.d2j.utils.database.entity.IEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.d2j.common.CollectionUtils.compare;

/**
 * User: Blackrush
 * Date: 03/12/11
 * Time: 17:49
 * IDE : IntelliJ IDEA
 */
public class Item implements IEntity<Long> {
    public static Collection<BaseItemType> toBaseItemType(Collection<Item> items){
        List<BaseItemType> result = new ArrayList<>(items.size());
        for (Item item : items){
            if (item.getQuantity() > 0){
                result.add(item.toBaseItemType());
            }
        }
        return result;
    }

    private long id;
    private ItemTemplate template;
    private Character owner;
    private Collection<ItemEffect> effects;
    private ItemPositionEnum position;
    private int quantity;

    public Item(ItemTemplate template, Collection<ItemEffect> effects) {
        this.template = template;
        this.effects = effects;
        this.position = ItemPositionEnum.NotEquiped;
        this.quantity = 1;
    }

    public Item(long id, ItemTemplate template, Character owner, Collection<ItemEffect> effects, ItemPositionEnum position, int quantity) {
        this.id = id;
        this.template = template;
        this.owner = owner;
        this.effects = effects;
        this.position = position;
        this.quantity = quantity;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public ItemTemplate getTemplate() {
        return template;
    }

    public Character getOwner() {
        return owner;
    }

    public void setOwner(Character owner) {
        this.owner = owner;
    }

    public Collection<ItemEffect> getEffects() {
        return effects;
    }

    public ItemPositionEnum getPosition() {
        return position;
    }

    public void setPosition(ItemPositionEnum position) {
        this.position = position;
    }

    public boolean isEquiped(){
        return position.equipment();
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Item addQuantity(int quantity) {
        this.quantity += quantity;
        return this;
    }

    public void apply(IStatistics statistics){
        for (ItemEffect stat : effects){
            stat.apply(statistics);
        }
    }

    public boolean equals(Item item){
        if (item.getTemplate() != template){
            return false;
        }

        return compare(item.effects, effects, new Comparator<ItemEffect>() {
            @Override
            public boolean compare(ItemEffect o1, ItemEffect o2) {
                return o1.equals(o2);
            }
        });
    }

    public Item copy(){
        Item copy = new Item(template, ItemEffect.copy(effects));
        copy.setId(id);
        return copy;
    }

    public BaseItemType toBaseItemType(){
        return new BaseItemType(
                id,
                template.getId(),
                quantity,
                position,
                ItemEffect.toBaseItemEffectType(effects)
        );
    }

    @Override
    public void beforeSave() {

    }

    @Override
    public void onSaved() {

    }
}
