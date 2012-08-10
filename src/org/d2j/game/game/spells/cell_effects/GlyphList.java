package org.d2j.game.game.spells.cell_effects;

import org.d2j.game.game.fights.AppendableFightHandlerAction;
import org.d2j.game.game.fights.FightException;
import org.d2j.game.game.fights.FightHandlerAction;
import org.d2j.game.game.fights.IFightHandler;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Blackrush
 * Date: 08/03/12
 * Time: 19:41
 */
public class GlyphList extends ArrayList<Glyph> {
    public void onTurnStopped(AppendableFightHandlerAction action) throws FightException {
        for (int i = 0; i < size(); ++i) { // avoid ConcurrentModificationException
            get(i).onTurnStopped(action);
        }
    }

    public void decrementRemainingTurns(AppendableFightHandlerAction action) {
        for (int i = 0; i < size(); ++i) { // avoid ConcurrentModificationException
            final Glyph glyph = get(i);
            glyph.decrementRemainingTurns();

            if (glyph.remainingTurns() <= 0){
                remove(i);

                glyph.delete();
                action.append(new FightHandlerAction() {
                    @Override
                    public void call(IFightHandler obj) throws FightException {
                        obj.notifyGlyphDeleted(glyph);
                    }
                });
            }
        }
    }
}
