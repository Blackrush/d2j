package org.d2j.game.game.items;

import org.d2j.common.client.protocol.TradeGameMessageFormatter;
import org.d2j.common.client.protocol.type.StoreItemType;
import org.d2j.game.model.*;
import org.d2j.game.model.Character;
import org.d2j.utils.AbstractCacheSystem;
import org.d2j.utils.DelegateCacheSystem;
import org.d2j.utils.Maker;
import org.d2j.utils.database.repository.IEntityRepository;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 23/02/12
 * Time: 18:53
 * To change this template use File | Settings | File Templates.
 */
public class StoreBag implements Iterable<StoredItem> {
    public static interface Listener {
        void listen(Character seller);
    }

    private final Character owner;
    private final IEntityRepository<StoredItem, Long> repository;
    private HashMap<Long, StoredItem> items = new HashMap<>();
    private boolean active;

    private List<Listener> listeners = Collections.synchronizedList(new ArrayList<Listener>());

    public StoreBag(Character owner, IEntityRepository<StoredItem, Long> repository) {
        this.owner = owner;
        this.repository = repository;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        boolean oldActive = this.active;
        this.active = active;

        if (!oldActive && active){
            owner.getCurrentMap().addSeller(owner);
        }
        else if (oldActive && !active){
            owner.getCurrentMap().removeSeller(owner);
        }

        for (int i = 0; i < listeners.size(); ++i){
            listeners.get(i).listen(owner);
        }
    }

    public void add(Item item, int quantity, long price){
        StoredItem sItem = new StoredItem(item, quantity, price);
        items.put(sItem.getId(), sItem);

        repository.create(sItem);
    }

    public void addEntity(StoredItem sItem){
        items.put(sItem.getId(), sItem);
    }

    public StoredItem remove(long itemId){
        StoredItem item = items.get(itemId);
        if (item != null){
            repository.delete(item);
            items.remove(itemId);
            return item;
        }
        return null;
    }

    public void remove(StoredItem item) {
        remove(item.getId());
    }

    public StoredItem get(long itemId){
        return items.get(itemId);
    }

    public int count(){
        return items.size();
    }

    public boolean empty(){
        return items.size() <= 0;
    }

    public void addListener(Listener listener){
        listeners.add(listener);
    }

    public void deleteListener(Listener listener){
        listeners.remove(listener);
    }

    public Collection<StoreItemType> toStoreItemType(){
        return StoredItem.toStoreItemType(items.values());
    }

    @Override
    public Iterator<StoredItem> iterator() {
        return items.values().iterator();
    }
}
