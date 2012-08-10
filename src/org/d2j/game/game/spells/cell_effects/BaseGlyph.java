package org.d2j.game.game.spells.cell_effects;

import org.d2j.game.game.fights.*;
import org.d2j.game.game.spells.ISpellLevel;
import org.d2j.game.game.spells.effects.Effect;
import org.d2j.game.game.spells.zones.Zone;
import org.d2j.game.model.SpellTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Blackrush
 * Date: 08/03/12
 * Time: 18:27
 */
public class BaseGlyph implements Glyph {
    public static Collection<Glyph> buildChildren(BaseGlyph trap, Fight fight, Zone zone){
        Collection<FightCell> cells = zone.filter(
                trap.getCell(), trap.getCell(),
                fight.getCells(),
                fight.getMap().getWidth(),
                fight.getMap().getHeight()
        );

        List<Glyph> children = new ArrayList<>(cells.size());
        children.add(trap);

        for (FightCell cell : cells){
            if (trap.getCell() == cell){
                cell.addEffect(trap);
            }
            else{
                children.add(new ChildGlyph(trap, cell));
            }
        }

        return children;
    }

    private final IFighter caster;
    private final FightCell cell;
    private final SpellTemplate originalSpell;
    private final ISpellLevel spell;
    private final Zone zone;
    private final int color;

    private Collection<Glyph> children;
    private int remainingTurns;

    public BaseGlyph(Fight fight, IFighter caster, FightCell cell, SpellTemplate originalSpell, ISpellLevel spell, Zone zone, int remainingTurns, int color) {
        this.caster = caster;
        this.cell = cell;
        this.originalSpell = originalSpell;
        this.spell = spell;
        this.zone = zone;
        this.remainingTurns = remainingTurns;
        this.color = color;
        this.children = buildChildren(this, fight, zone);
    }

    @Override
    public FightCell getCell() {
        return cell;
    }

    @Override
    public void add() {
        for (Glyph glyph : children) {
            glyph.getCell().addEffect(glyph);
        }
    }

    @Override
    public void delete() {
        for (Glyph glyph : children) {
            glyph.getCell().removeEffect(glyph);
        }
    }

    @Override
    public FightCell getBaseCell() {
        return cell;
    }

    @Override
    public IFighter getCaster() {
        return caster;
    }

    @Override
    public Zone getZone() {
        return zone;
    }

    @Override
    public SpellTemplate getOriginalSpell() {
        return originalSpell;
    }

    @Override
    public int getColor() {
        return color;
    }

    @Override
    public int remainingTurns() {
        return remainingTurns;
    }

    @Override
    public void decrementRemainingTurns() {
        --remainingTurns;
    }

    @Override
    public void onFighterWalkedOn(AppendableFightHandlerAction action, IFighter trigger) throws FightException {
    }

    @Override
    public void onTurnStopped(AppendableFightHandlerAction action) throws FightException {
        for (Glyph glyph : children) {
            for (Effect effect : spell.getEffects()) {
                effect.apply(action, caster, glyph.getCell());
            }
        }
    }
}
