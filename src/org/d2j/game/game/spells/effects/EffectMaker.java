package org.d2j.game.game.spells.effects;

import org.d2j.game.game.spells.ISpellLevel;

/**
 * User: Blackrush
 * Date: 14/12/11
 * Time: 16:36
 * IDE : IntelliJ IDEA
 */
public interface EffectMaker {
    Effect make(ISpellLevel infos);
}
