package org.d2j.game.game.spells.cell_effects;

import org.d2j.game.game.fights.FightCell;
import org.d2j.game.game.fights.IFighter;
import org.d2j.game.game.spells.zones.Zone;
import org.d2j.game.model.SpellTemplate;

/**
 * Created by IntelliJ IDEA.
 * User: Blackrush
 * Date: 08/03/12
 * Time: 19:04
 */
public interface Glyph extends CellEffect {
    FightCell getBaseCell();
    IFighter getCaster();
    Zone getZone();
    SpellTemplate getOriginalSpell();
    int getColor();

    int remainingTurns();
    void decrementRemainingTurns();

    void delete();
}
