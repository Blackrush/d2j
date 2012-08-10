package org.d2j.game.game.spells.filters;

import org.d2j.game.game.fights.IFighter;

/**
 * Created by IntelliJ IDEA.
 * User: Blackrush
 * Date: 05/03/12
 * Time: 13:03
 */
public interface Filter {
    boolean filter(IFighter caster, IFighter target);
}
