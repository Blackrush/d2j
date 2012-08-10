package org.d2j.game.model;

import org.d2j.common.client.protocol.type.StoreItemType;
import org.d2j.game.game.items.ItemEffect;
import org.d2j.utils.database.entity.IEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 23/02/12
 * Time: 19:08
 * To change this template use File | Settings | File Templates.
 */
public class StoredItem implements IEntity<Long> {
    public static Collection<StoreItemType> toStoreItemType(Collection<StoredItem> items){
        List<StoreItemType> result = new ArrayList<>(items.size());
        for (StoredItem item : items){
            result.add(item.toStoreItemType());
        }
        return result;
    }

    private long id;
    private Item item;
    private int quantity;
    private long price;

    public StoredItem(Item item, int quantity, long price) {
        this.id = item.getId();
        this.item = item;
        this.quantity = quantity;
        this.price = price;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public StoredItem addQuantity(int quantity) {
        this.quantity += quantity;
        return this;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public StoreItemType toStoreItemType(){
        return new StoreItemType(
                item.getId(),
                item.getTemplate().getId(),
                quantity,
                item.getPosition(),
                ItemEffect.toBaseItemEffectType(item.getEffects()),
                price
        );
    }

    @Override
    public void beforeSave() {
    }

    @Override
    public void onSaved() {
    }
}
