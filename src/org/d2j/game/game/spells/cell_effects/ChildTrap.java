package org.d2j.game.game.spells.cell_effects;

import org.d2j.game.game.fights.AppendableFightHandlerAction;
import org.d2j.game.game.fights.FightCell;
import org.d2j.game.game.fights.FightException;
import org.d2j.game.game.fights.IFighter;
import org.d2j.game.game.spells.zones.Zone;
import org.d2j.game.model.SpellTemplate;

/**
 * Created by IntelliJ IDEA.
 * User: Blackrush
 * Date: 07/03/12
 * Time: 14:31
 */
public class ChildTrap implements Trap {
    private final BaseTrap parent;
    private final FightCell cell;

    public ChildTrap(BaseTrap parent, FightCell cell) {
        this.parent = parent;
        this.cell = cell;
    }

    @Override
    public FightCell getCell() {
        return cell;
    }

    @Override
    public IFighter getCaster() {
        return parent.getCaster();
    }

    @Override
    public FightCell getBaseCell() {
        return parent.getCell();
    }

    @Override
    public SpellTemplate getOriginalSpell() {
        return parent.getOriginalSpell();
    }

    @Override
    public Zone getZone() {
        return parent.getZone();
    }

    @Override
    public void onFighterWalkedOn(AppendableFightHandlerAction action, IFighter trigger) throws FightException {
        parent.onFighterWalkedOn(action, trigger);
    }

    @Override
    public void onTurnStopped(AppendableFightHandlerAction action) throws FightException {
    }
}
