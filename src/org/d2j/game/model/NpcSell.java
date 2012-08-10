package org.d2j.game.model;

import org.d2j.common.client.protocol.type.BaseItemTemplateType;
import org.d2j.utils.database.entity.IBaseEntity;
import org.d2j.utils.database.entity.IEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 01/02/12
 * Time: 19:31
 * To change this template use File | Settings | File Templates.
 */
public class NpcSell implements IEntity<Integer> {
    public static Collection<BaseItemTemplateType> toBaseItemTemplateType(Collection<NpcSell> sells){
        List<BaseItemTemplateType> result = new ArrayList<>(sells.size());
        for (NpcSell sell : sells){
            result.add(sell.getItem().toBaseItemTemplateType());
        }
        return result;
    }

    private int id;
    private NpcTemplate npc;
    private ItemTemplate item;

    public NpcSell(NpcTemplate npc, ItemTemplate item) {
        this.npc = npc;
        this.item = item;
    }

    public NpcSell(int id, NpcTemplate npc, ItemTemplate item) {
        this.id = id;
        this.npc = npc;
        this.item = item;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public NpcTemplate getNpc() {
        return npc;
    }

    public void setNpc(NpcTemplate npc) {
        this.npc = npc;
    }

    public ItemTemplate getItem() {
        return item;
    }

    public void setItem(ItemTemplate item) {
        this.item = item;
    }

    @Override
    public void beforeSave() {
    }

    @Override
    public void onSaved() {
    }
}
