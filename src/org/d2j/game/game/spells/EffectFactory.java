package org.d2j.game.game.spells;

/**
 * User: Blackrush
 * Date: 08/12/11
 * Time: 20:19
 * IDE : IntelliJ IDEA
 */
public class EffectFactory {
    public static IEffectFactory getInstance(){
        return DefaultEffectFactory.getInstance();
    }
}
