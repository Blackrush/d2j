package org.d2j.game.repository;

import org.d2j.game.game.spells.EffectFactory;
import org.d2j.game.model.SpellTemplate;
import org.d2j.utils.database.EntitiesContext;
import org.d2j.utils.database.annotation.Static;
import org.d2j.utils.database.repository.AbstractBaseEntityRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * User: Blackrush
 * Date: 08/12/11
 * Time: 16:59
 * IDE : IntelliJ IDEA
 */
@Singleton
public class SpellTemplateRepository extends AbstractBaseEntityRepository<SpellTemplate, Integer> {
    @Inject
    public SpellTemplateRepository(@Static EntitiesContext context) {
        super(context);
    }

    @Override
    protected String getLoadQuery() {
        return "SELECT * FROM `spell_templates`;";
    }

    @Override
    protected String getLoadOneQuery(Integer id) {
        return "SELECT * FROM `spell_templates` WHERE `id`='" + id + "';";
    }

    @Override
    protected SpellTemplate loadOne(ResultSet reader) throws SQLException {
        SpellTemplate spell = new SpellTemplate(
                reader.getInt("id"),
                reader.getString("name"),
                reader.getInt("sprite"),
                reader.getString("spriteInfos")
        );

        spell.getLevels().put((short)1, EffectFactory.getInstance().parse(spell, reader.getString("first")));
        spell.getLevels().put((short)2, EffectFactory.getInstance().parse(spell, reader.getString("second")));
        spell.getLevels().put((short)3, EffectFactory.getInstance().parse(spell, reader.getString("third")));
        spell.getLevels().put((short)4, EffectFactory.getInstance().parse(spell, reader.getString("fourth")));
        spell.getLevels().put((short)5, EffectFactory.getInstance().parse(spell, reader.getString("fifth")));
        spell.getLevels().put((short)6, EffectFactory.getInstance().parse(spell, reader.getString("sixth")));

        return spell;
    }
}
