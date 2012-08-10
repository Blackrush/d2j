package org.d2j.game.game.items;

import org.d2j.common.client.protocol.enums.ItemEffectEnum;
import org.d2j.common.client.protocol.type.BaseItemTemplateEffectType;
import org.d2j.common.random.Dice;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * User: Blackrush
 * Date: 24/12/11
 * Time: 11:57
 * IDE : IntelliJ IDEA
 */
public class ItemEffectTemplate {
    public static Collection<ItemEffect> generate(Collection<ItemEffectTemplate> effects){
        List<ItemEffect> result = new ArrayList<>(effects.size());
        for (ItemEffectTemplate effect : effects){
            result.add(effect.generate());
        }
        return result;
    }

    public static Collection<BaseItemTemplateEffectType> toBaseItemTemplateEffectType(Collection<ItemEffectTemplate> effects){
        List<BaseItemTemplateEffectType> result = new ArrayList<>(effects.size());
        for (ItemEffectTemplate effect : effects){
            result.add(effect.toBaseItemTemplateEffectType());
        }
        return result;
    }

    protected final ItemEffectEnum effect;
    protected final Dice dice;

    public ItemEffectTemplate(ItemEffectEnum effect, Dice dice) {
        this.effect = effect;
        this.dice = dice;
    }

    public ItemEffectEnum getEffect() {
        return effect;
    }

    public Dice getDice() {
        return dice;
    }

    public ItemEffect generate(){
        return new ItemEffect(
                effect,
                (short) dice.roll()
        );
    }

    public BaseItemTemplateEffectType toBaseItemTemplateEffectType(){
        return new BaseItemTemplateEffectType(
                effect,
                dice.getRound(), dice.getNum(), dice.getAdd(),
                dice
        );
    }
}
