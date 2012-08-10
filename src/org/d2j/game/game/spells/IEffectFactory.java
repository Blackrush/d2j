package org.d2j.game.game.spells;

import org.d2j.common.client.protocol.enums.ItemEffectEnum;
import org.d2j.common.client.protocol.enums.SpellEffectsEnum;
import org.d2j.game.game.spells.effects.Effect;
import org.d2j.game.model.SpellTemplate;
import org.d2j.utils.database.repository.IBaseEntityRepository;

/**
 * User: Blackrush
 * Date: 08/12/11
 * Time: 20:19
 * IDE : IntelliJ IDEA
 */
public interface IEffectFactory {
    ISpellLevel parse(SpellTemplate template, String str);
    Effect get(SpellEffectsEnum effect);
    void load(IBaseEntityRepository<SpellTemplate, Integer> spells);
}
