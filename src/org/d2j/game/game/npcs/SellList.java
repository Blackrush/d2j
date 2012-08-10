package org.d2j.game.game.npcs;

import org.d2j.common.CollectionUtils;
import org.d2j.common.client.protocol.TradeGameMessageFormatter;
import org.d2j.common.client.protocol.type.BaseItemTemplateType;
import org.d2j.game.model.ItemTemplate;
import org.d2j.game.model.NpcSell;
import org.d2j.game.model.NpcTemplate;
import org.d2j.game.repository.NpcSellRepository;
import org.d2j.utils.AbstractCacheSystem;
import org.d2j.utils.DelegateCacheSystem;
import org.d2j.utils.Maker;
import org.d2j.utils.database.repository.IEntityRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 01/02/12
 * Time: 19:16
 * To change this template use File | Settings | File Templates.
 */
public class SellList {
    public class Cache extends AbstractCacheSystem<String> {
        @Override
        protected void refresh() {
            obj = TradeGameMessageFormatter.itemListMessage(NpcSell.toBaseItemTemplateType(datas.values()));
        }
    }

    private final NpcTemplate npc;
    private final IEntityRepository<NpcSell, Integer> npcSells;

    private HashMap<Integer, NpcSell> datas = new HashMap<>();
    private Cache cache;

    public SellList(NpcTemplate npc, IEntityRepository<NpcSell, Integer> npcSells) {
        this.npc = npc;
        this.npcSells = npcSells;
        this.cache = new Cache();
    }

    public Cache getCache(){
        return cache;
    }

    public void addData(NpcSell data){
        datas.put(data.getItem().getId(), data);
    }

    public void add(ItemTemplate item){
        NpcSell sell = new NpcSell(npc, item);
        npcSells.create(sell);

        datas.put(item.getId(), sell);

        cache.setRefresh();
    }

    public boolean remove(Integer itemId){
        NpcSell sell = datas.get(itemId);
        if (sell == null){
            return false;
        }

        npcSells.delete(sell);
        datas.remove(itemId);

        cache.setRefresh();

        return true;
    }

    public ItemTemplate get(Integer itemId){
        NpcSell data = datas.get(itemId);
        return data != null ? data.getItem() : null;
    }
}
