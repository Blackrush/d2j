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
 * Date: 07/03/12
 * Time: 14:28
 */
public class BaseTrap implements Trap {
    public static Collection<Trap> buildChildren(BaseTrap trap, Fight fight, Zone zone){
        Collection<FightCell> cells = zone.filter(
                trap.getCell(), trap.getCell(),
                fight.getCells(),
                fight.getMap().getWidth(),
                fight.getMap().getHeight()
        );

        List<Trap> children = new ArrayList<>(cells.size());
        children.add(trap);

        for (FightCell cell : cells){
            if (trap.getCell() == cell){
                cell.addEffect(trap);
            }
            else{
                children.add(new ChildTrap(trap, cell));
            }
        }

        return children;
    }

    private final IFighter caster;
    private final SpellTemplate original;
    private final FightCell cell;
    private final ISpellLevel spell;
    private final Zone zone;

    private Collection<Trap> children;

    public BaseTrap(Fight fight, IFighter caster, SpellTemplate original, FightCell cell, ISpellLevel spell, Zone zone) {
        this.caster = caster;
        this.original = original;
        this.cell = cell;
        this.spell = spell;
        this.zone = zone;
        this.children = buildChildren(this, fight, zone);
    }

    @Override
    public FightCell getCell() {
        return cell;
    }

    @Override
    public void add() {
        for (Trap trap : children) {
            trap.getCell().addEffect(trap);
        }
    }

    @Override
    public void delete() {
        for (Trap trap : children) {
            trap.getCell().removeEffect(trap);
        }
    }

    @Override
    public IFighter getCaster() {
        return caster;
    }

    @Override
    public FightCell getBaseCell() {
        return cell;
    }

    @Override
    public SpellTemplate getOriginalSpell() {
        return original;
    }

    @Override
    public Zone getZone() {
        return zone;
    }

    @Override
    public void onFighterWalkedOn(AppendableFightHandlerAction action, final IFighter trigger) throws FightException {
        action.append(new FightHandlerAction() {
            @Override
            public void call(IFightHandler obj) throws FightException {
                obj.notifyTrapUsed(trigger, BaseTrap.this);
            }
        });

        for (Trap child : children) {
            for (Effect effect : spell.getEffects()){
                effect.apply(action, caster, child.getCell());
            }
        }

        action.append(new FightHandlerAction() {
            @Override
            public void call(IFightHandler obj) throws FightException {
                obj.notifyTrapDeleted(BaseTrap.this);
            }
        });
    }

    @Override
    public void onTurnStopped(AppendableFightHandlerAction action) throws FightException {
    }
}
