package org.d2j.game.game.spells.filters;

import org.d2j.game.game.fights.IFighter;

/**
 * Created by IntelliJ IDEA.
 * User: Blackrush
 * Date: 05/03/12
 * Time: 13:03
 */
public class TargetFilter implements Filter {   
    public static TargetFilter parseTargetFilter(int integer){
        return new TargetFilter(
                (integer & 1)  != 0,
                (integer & 2)  != 0,
                (integer & 4)  != 0,
                (integer & 8)  != 0,
                (integer & 16) != 0
        );
    }

    public static TargetFilter newDefault(){
        return new TargetFilter(true, true, true, false, false);
    }

    private boolean ennemies, allies, caster, iCaster, invocations;

    public TargetFilter() {
    }

    public TargetFilter(boolean ennemies, boolean allies, boolean caster, boolean iCaster, boolean invocations) {
        this.ennemies = ennemies;
        this.allies = allies;
        this.caster = caster;
        this.iCaster = iCaster;
        this.invocations = invocations;
    }

    @Override
    public boolean filter(IFighter caster, IFighter target) {
        return (ennemies && caster.getTeam().getTeamType() != target.getTeam().getTeamType()) ||
               (allies && caster.getTeam().getTeamType() == target.getTeam().getTeamType())   ||
               (this.caster && target == caster);
    }
}
