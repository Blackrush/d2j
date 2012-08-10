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
 * Date: 08/03/12
 * Time: 18:28
 */
public class ChildGlyph implements Glyph {
    private final BaseGlyph parent;
    private final FightCell cell;

    public ChildGlyph(BaseGlyph parent, FightCell cell) {
        this.parent = parent;
        this.cell = cell;
    }

    @Override
    public FightCell getCell() {
        return cell;
    }

    @Override
    public FightCell getBaseCell() {
        return parent.getCell();
    }

    @Override
    public IFighter getCaster() {
        return parent.getCaster();
    }

    @Override
    public Zone getZone() {
        return parent.getZone();
    }

    @Override
    public SpellTemplate getOriginalSpell() {
        return parent.getOriginalSpell();
    }

    @Override
    public int getColor() {
        return parent.getColor();
    }

    @Override
    public int remainingTurns() {
        return parent.remainingTurns();
    }

    @Override
    public void decrementRemainingTurns() {
        parent.decrementRemainingTurns();
    }

    @Override
    public void onFighterWalkedOn(AppendableFightHandlerAction action, IFighter trigger) throws FightException {
    }

    @Override
    public void onTurnStopped(AppendableFightHandlerAction action) throws FightException {
        parent.onTurnStopped(action);
    }

    public void delete() {
        parent.delete();
    }
}
