package org.d2j.game.game.items;

import org.d2j.game.model.*;
import org.d2j.game.model.Character;
import org.d2j.utils.database.repository.IEntityRepository;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 13/01/12
 * Time: 18:44
 * To change this template use File | Settings | File Templates.
 */
public class PersistentBag extends Bag {
    private final IEntityRepository<Item, Long> items;

    public PersistentBag(Character owner, IEntityRepository<Item, Long> items) {
        super(owner);
        this.items = items;
    }

    @Override
    public void add(Item item) {
        items.create(item);
        super.add(item);
    }

    @Override
    public Item remove(Object key) {
        Item item = get(key);
        if (item != null){
            super.remove(key);
            items.delete(item);
        }
        return item;
    }

    public Item removeEntity(Long key){
        return super.remove(key);
    }

    @Override
    public boolean factorize(Item item) {
        if (!super.factorize(item)){
            item.setOwner(owner);
            items.create(item);
            put(item.getId(), item);
            return false;
        }
        return true;
    }
}
