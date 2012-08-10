package org.d2j.game.game.spells;

import org.d2j.game.game.spells.ISpellLevel;
import org.d2j.game.game.spells.effects.Effect;
import org.d2j.game.game.spells.zones.Zone;
import org.d2j.game.model.SpellTemplate;

import java.util.Collection;

/**
 * User: Blackrush
 * Date: 15/12/11
 * Time: 15:54
 * IDE : IntelliJ IDEA
 */
public class SpellLevel implements ISpellLevel {
    private final SpellTemplate template;

    private Collection<Effect> effects;
    private Collection<Effect> criticalEffects;
    private short cost;
    private short minRange, maxRange;
    private short criticRate, criticalFailRate;
    private boolean inline, lov, emptyCell, modifiableRange;
    private int maxPerTurn, maxPerPlayer;
    private Zone zone, criticalZone;

    public SpellLevel(SpellTemplate template, short cost, short minRange, short maxRange, short criticRate, short criticalFailRate, boolean inline, boolean lov, boolean emptyCell, boolean modifiableRange, int maxPerTurn, int maxPerPlayer) {
        this.template = template;
        this.cost = cost;
        this.minRange = minRange;
        this.maxRange = maxRange;
        this.criticRate = criticRate;
        this.criticalFailRate = criticalFailRate;
        this.inline = inline;
        this.lov = lov;
        this.emptyCell = emptyCell;
        this.modifiableRange = modifiableRange;
        this.maxPerTurn = maxPerTurn;
        this.maxPerPlayer = maxPerPlayer;
    }

    @Override
    public SpellTemplate getTemplate() {
        return template;
    }

    @Override
    public Collection<Effect> getEffects() {
        return effects;
    }

    public void setEffects(Collection<Effect> effects) {
        this.effects = effects;
    }

    @Override
    public Collection<Effect> getCriticalEffects() {
        return criticalEffects;
    }

    public void setCriticalEffects(Collection<Effect> criticalEffects) {
        this.criticalEffects = criticalEffects;
    }

    @Override
    public short getCost() {
        return cost;
    }

    @Override
    public short getMinRange() {
        return minRange;
    }

    @Override
    public short getMaxRange() {
        return maxRange;
    }

    @Override
    public short getCriticRate() {
        return criticRate;
    }

    @Override
    public short getCriticalFailRate() {
        return criticalFailRate;
    }

    @Override
    public boolean isInline() {
        return inline;
    }

    @Override
    public boolean getLineOfVision() {
        return lov;
    }

    @Override
    public boolean isEmptyCell() {
        return emptyCell;
    }

    @Override
    public boolean isModifiableRange() {
        return modifiableRange;
    }

    @Override
    public int getMaxPerTurn() {
        return maxPerTurn;
    }

    @Override
    public int getMaxPerPlayer() {
        return maxPerPlayer;
    }
}
