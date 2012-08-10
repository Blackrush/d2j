package org.d2j.game.game.items;

import org.d2j.common.client.protocol.enums.ItemPositionEnum;
import org.d2j.common.client.protocol.enums.ItemTypeEnum;
import org.d2j.common.client.protocol.type.BaseItemType;
import org.d2j.game.model.Character;
import org.d2j.game.model.Item;
import org.d2j.game.model.ItemTemplate;
import org.d2j.utils.database.repository.IEntityRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * User: Blackrush
 * Date: 24/12/11
 * Time: 12:45
 * IDE : IntelliJ IDEA
 */
public class Bag extends HashMap<Long, Item> {
    public static boolean validMovement(ItemTemplate template, ItemPositionEnum target){
        switch (target) {
            case Amulet:
                return template.getType() == ItemTypeEnum.Amulet;

            case Weapon:
                return template.isWeapon();

            case RightRing:
            case LeftRing:
                return template.getType() == ItemTypeEnum.Ring;

            case Belt:
                return template.getType() == ItemTypeEnum.Belt;

            case Boot:
                return template.getType() == ItemTypeEnum.Boot;

            case Hat:
                return template.getType() == ItemTypeEnum.Hat;

            case Cloak:
                return template.getType() == ItemTypeEnum.Cloack;

            case Pet:
                return template.getType() == ItemTypeEnum.Pet;

            case Dofus1:
            case Dofus2:
            case Dofus3:
            case Dofus4:
            case Dofus5:
            case Dofus6:
                return template.getType() == ItemTypeEnum.Dofus;

            case Shield:
                return template.getType() == ItemTypeEnum.Shield;

            default:
                return true;
        }
    }

    public static boolean canWearRing(Bag bag, ItemPositionEnum position, ItemTemplate template){
        if (position == ItemPositionEnum.LeftRing){
            Item item = bag.get(ItemPositionEnum.RightRing);
            return item == null || item.getTemplate() != template;
        }
        else if (position == ItemPositionEnum.RightRing){
            Item item = bag.get(ItemPositionEnum.LeftRing);
            return item == null || item.getTemplate() != template;
        }
        else{
            return true;
        }
    }

    protected final Character owner;
    private long kamas;

    public Bag(Character owner) {
        this.owner = owner;
    }

    public long getKamas() {
        return kamas;
    }

    public void setKamas(long kamas) {
        this.kamas = kamas;
    }

    public Bag addKamas(long kamas) {
        this.kamas += kamas;
        return this;
    }

    public void add(Item item){
        put(item.getId(), item);
    }

    public Item get(ItemPositionEnum position){
        for (Item item : values()){
            if (item.getPosition().equals(position)){
                return item;
            }
        }
        return null;
    }

    public boolean has(ItemPositionEnum position){
        for (Item item : values()){
            if (item.getPosition().equals(position)){
                return true;
            }
        }
        return false;
    }

    public void factorize(){
        if (size() <= 1) return;

        List<Item> items = new ArrayList<>(values());

        while (items.size() > 1){
            Item item = items.get(0);
            items.remove(0);

            for (int i = 0; i < items.size(); ++i){
                Item current = items.get(i);
                if (current == item || !current.equals(item)){
                    continue;
                }

                item.addQuantity(current.getQuantity());
                item.setOwner(null);
                items.remove(i);
            }
        }
    }

    public boolean factorize(Item item){
        for (Item i : values()){
            if (item.equals(i)){
                i.addQuantity(item.getQuantity());
                return true;
            }
        }
        return false;
    }

    public Item[] getAccessories(){
        Item[] accessories = new Item[5];

        accessories[0] = get(ItemPositionEnum.Weapon);
        accessories[1] = get(ItemPositionEnum.Hat);
        accessories[2] = get(ItemPositionEnum.Cloak);
        accessories[3] = get(ItemPositionEnum.Pet);
        accessories[4] = get(ItemPositionEnum.Shield);

        return accessories;
    }

    public long contains(Item item){
        for (Item itm : values()){
            if (item.equals(itm)){
                return itm.getId();
            }
        }
        return -1;
    }

    public Item getSame(Item item){
        for (Item itm : values()){
            if (item.equals(itm)){
                return itm;
            }
        }
        return null;
    }

    public boolean isFull(){
        return owner.getStatistics().getUsedPods() >= owner.getStatistics().getMaxPods();
    }

    public boolean isAvailable(ItemPositionEnum position){
        for (Item item : values()){
            if (item.getPosition().equals(position)){
                return false;
            }
        }
        return true;
    }

    public Collection<Item> getEquipedItems(){
        List<Item> result = new ArrayList<>(8);
        for (Item item : values()){
            if (item.isEquiped()){
                result.add(item);
            }
        }
        return result;
    }

    public Item getByTemplate(ItemTemplate template) {
        for (Item item : values()){
            if (item.getTemplate().equals(template)){
                return item;
            }
        }
        return null;
    }

    public Collection<BaseItemType> toBaseItemType(){
        return Item.toBaseItemType(values());
    }
}
