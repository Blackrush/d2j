package org.d2j.common.client.protocol.type;

import org.d2j.common.client.protocol.enums.ItemPositionEnum;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 23/02/12
 * Time: 18:49
 * To change this template use File | Settings | File Templates.
 */
public class StoreItemType extends BaseItemType {
    private long price;

    public StoreItemType(long id, int templateId, int quantity, ItemPositionEnum position, Collection<BaseItemEffectType> effects, long price) {
        super(id, templateId, quantity, position, effects);
        this.price = price;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }
}
