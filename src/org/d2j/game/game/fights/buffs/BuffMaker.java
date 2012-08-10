package org.d2j.game.game.fights.buffs;

import org.d2j.game.game.fights.IFighter;

/**
 * Created by IntelliJ IDEA.
 * User: Blackrush
 * Date: 01/03/12
 * Time: 20:37
 */
public interface BuffMaker {
    Buff make(IFighter caster, IFighter target);
}
