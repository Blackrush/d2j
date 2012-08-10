package org.d2j.game.game.fights.buffs;

import org.d2j.common.client.protocol.enums.SpellEffectsEnum;
import org.d2j.game.game.fights.AppendableFightHandlerAction;
import org.d2j.game.game.fights.FightException;
import org.d2j.game.game.fights.IFighter;
import org.d2j.game.model.SpellTemplate;

/**
 * Created by IntelliJ IDEA.
 * User: Blackrush
 * Date: 01/03/12
 * Time: 20:41
 */
public abstract class AbstractBuff implements Buff {
    protected final IFighter caster, target;
    protected final SpellTemplate spell;

    private int remainingTurns;

    protected AbstractBuff(IFighter caster, IFighter target, SpellTemplate spell, int remainingTurns) {
        this.caster = caster;
        this.target = target;
        this.spell = spell;
        this.remainingTurns = remainingTurns;
    }

    @Override
    public IFighter getCaster() {
        return caster;
    }

    @Override
    public IFighter getTarget() {
        return target;
    }

    @Override
    public SpellTemplate getSpell() {
        return spell;
    }

    @Override
    public int getRemainingTurns() {
        return remainingTurns;
    }

    protected void decrementRemainingTurns(){
        --remainingTurns;
    }
}
