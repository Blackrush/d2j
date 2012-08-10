package org.d2j.common.client.protocol.type;

import org.d2j.common.client.protocol.enums.ItemEffectEnum;
import org.d2j.common.random.Dice;
import org.d2j.game.game.items.ItemEffect;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 01/02/12
 * Time: 18:43
 * To change this template use File | Settings | File Templates.
 */
public class BaseItemTemplateEffectType {
    private ItemEffectEnum effect;
    private int value1, value2, value3;
    private Dice dice;

    public BaseItemTemplateEffectType() {
    }

    public BaseItemTemplateEffectType(ItemEffectEnum effect, int value1, int value2, int value3, Dice dice) {
        this.effect = effect;
        this.value1 = value1;
        this.value2 = value2;
        this.value3 = value3;
        this.dice = dice;
    }

    public ItemEffectEnum getEffect() {
        return effect;
    }

    public void setEffect(ItemEffectEnum effect) {
        this.effect = effect;
    }

    public int getValue1() {
        return value1;
    }

    public void setValue1(int value1) {
        this.value1 = value1;
    }

    public int getValue2() {
        return value2;
    }

    public void setValue2(int value2) {
        this.value2 = value2;
    }

    public int getValue3() {
        return value3;
    }

    public void setValue3(int value3) {
        this.value3 = value3;
    }

    public Dice getDice() {
        return dice;
    }

    public void setDice(Dice dice) {
        this.dice = dice;
    }
}
