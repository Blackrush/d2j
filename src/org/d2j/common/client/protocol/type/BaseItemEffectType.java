package org.d2j.common.client.protocol.type;

import org.d2j.common.client.protocol.enums.ItemEffectEnum;

/**
 * User: Blackrush
 * Date: 26/12/11
 * Time: 20:50
 * IDE : IntelliJ IDEA
 */
public class BaseItemEffectType {
    private ItemEffectEnum effect;
    private short bonus;

    public BaseItemEffectType(ItemEffectEnum effect, short bonus) {
        this.effect = effect;
        this.bonus = bonus;
    }

    public ItemEffectEnum getEffect() {
        return effect;
    }

    public void setEffect(ItemEffectEnum effect) {
        this.effect = effect;
    }

    public short getBonus() {
        return bonus;
    }

    public void setBonus(short bonus) {
        this.bonus = bonus;
    }
}
