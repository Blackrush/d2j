package org.d2j.game.game.spells.cell_effects;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Blackrush
 * Date: 08/03/12
 * Time: 20:20
 */
public class CellEffectList extends ArrayList<CellEffect> {
    public int nbTraps() {
        int result = 0;
        for (int i = 0; i < size(); ++i) {
            if (get(i) instanceof Trap) {
                ++result;
            }
        }
        return result;
    }
}
