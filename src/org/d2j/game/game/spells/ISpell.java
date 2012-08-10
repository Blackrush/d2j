package org.d2j.game.game.spells;

import org.d2j.game.model.SpellTemplate;

/**
 * User: Blackrush
 * Date: 17/12/11
 * Time: 14:40
 * IDE : IntelliJ IDEA
 */
public interface ISpell {
    SpellTemplate getTemplate();
    short getLevel();
}
