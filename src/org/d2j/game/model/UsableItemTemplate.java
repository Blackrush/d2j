package org.d2j.game.model;

import org.d2j.common.client.protocol.enums.ItemTypeEnum;
import org.d2j.game.game.actions.GameActionException;
import org.d2j.game.game.items.ItemEffectTemplate;
import org.d2j.game.game.live_actions.ILiveAction;
import org.d2j.game.service.game.GameClient;

import java.util.Collection;

/**
 * User: Blackrush
 * Date: 23/12/11
 * Time: 23:29
 * IDE : IntelliJ IDEA
 */
public class UsableItemTemplate extends ItemTemplate {
    private Collection<ILiveAction> effects;
    private boolean targetable;

    public UsableItemTemplate(int id, String name, ItemSetTemplate itemSet, ItemTypeEnum type, short level, int weight, boolean forgemageable, int price, String conditions, Collection<ItemEffectTemplate> stats, Collection<ILiveAction> effects, boolean targetable) {
        super(id, name, itemSet, type, level, weight, forgemageable, price, conditions, stats);
        this.effects = effects;
        this.targetable = targetable;
    }

    @Override
    public boolean isUsable() {
        return true;
    }

    @Override
    public boolean isTargetable() {
        return targetable;
    }

    public void use(GameClient client) throws GameActionException {
        for (ILiveAction effect : effects){
            effect.apply(client);
        }
    }
}
