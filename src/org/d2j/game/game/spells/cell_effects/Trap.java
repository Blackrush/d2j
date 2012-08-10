package org.d2j.game.game.spells.cell_effects;

import org.d2j.game.game.fights.FightCell;
import org.d2j.game.game.fights.IFighter;
import org.d2j.game.game.spells.zones.Zone;
import org.d2j.game.model.SpellTemplate;

/**
 * Created by IntelliJ IDEA.
 * User: Blackrush
 * Date: 07/03/12
 * Time: 14:54
 */
public interface Trap extends CellEffect {
    IFighter getCaster();
    FightCell getBaseCell();
    SpellTemplate getOriginalSpell();
    Zone getZone();
}
