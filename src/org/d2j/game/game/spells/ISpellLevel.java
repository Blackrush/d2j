package org.d2j.game.game.spells;

import org.d2j.game.game.spells.effects.Effect;
import org.d2j.game.game.spells.zones.Zone;
import org.d2j.game.model.SpellTemplate;

import java.util.Collection;

/**
 * User: Blackrush
 * Date: 14/12/11
 * Time: 16:53
 * IDE : IntelliJ IDEA
 */
public interface ISpellLevel {
    SpellTemplate getTemplate();

    Collection<Effect> getEffects();
    Collection<Effect> getCriticalEffects();

    short getCost();
    short getMinRange();
    short getMaxRange();
    short getCriticRate();
    short getCriticalFailRate();
    boolean isInline();
    boolean getLineOfVision();
    boolean isEmptyCell();
    boolean isModifiableRange();
    int getMaxPerTurn();
    int getMaxPerPlayer();
}
