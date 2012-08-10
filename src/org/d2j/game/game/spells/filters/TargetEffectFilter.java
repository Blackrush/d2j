package org.d2j.game.game.spells.filters;

import org.d2j.game.game.fights.IFighter;

/**
 * Created by IntelliJ IDEA.
 * User: Blackrush
 * Date: 05/03/12
 * Time: 13:05
 */
public class TargetEffectFilter implements Filter {
    private final Filter filter;

    public TargetEffectFilter(Filter filter) {
        this.filter = filter;
    }

    @Override
    public boolean filter(IFighter caster, IFighter target) {
        return target != null && target.isAlive() && filter.filter(caster, target);
    }
}
