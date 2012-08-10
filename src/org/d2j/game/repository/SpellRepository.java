package org.d2j.game.repository;

import org.d2j.common.StringUtils;
import org.d2j.game.model.Character;
import org.d2j.game.model.Spell;
import org.d2j.game.model.SpellTemplate;
import org.d2j.utils.database.EntitiesContext;
import org.d2j.utils.database.annotation.Dynamic;
import org.d2j.utils.database.repository.AbstractEntityRepository;
import org.d2j.utils.database.repository.IBaseEntityRepository;
import org.d2j.utils.database.repository.IEntityRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * User: Blackrush
 * Date: 08/12/11
 * Time: 18:07
 * IDE : IntelliJ IDEA
 */
@Singleton
public class SpellRepository extends AbstractEntityRepository<Spell, Long> {
    private long nextId;

    private final IBaseEntityRepository<SpellTemplate, Integer> spellTemplates;
    private final IEntityRepository<Character, Long> characters;

    @Inject
    public SpellRepository(@Dynamic EntitiesContext context, IBaseEntityRepository<SpellTemplate, Integer> spellTemplates, IEntityRepository<Character, Long> characters) {
        super(context);
        this.spellTemplates = spellTemplates;
        this.characters = characters;
    }

    @Override
    protected void setNextId(Spell entity) {
        entity.setId(++nextId);
    }

    @Override
    protected String getCreateQuery(Spell entity) {
        return StringUtils.format(
                "INSERT INTO `spell_characters`(`id`,`character`,`spell`,`position`,`level`) VALUES(" +
                        "'{0}', '{1}', '{2}', '{3}', '{4}'" +
                        ");",

                entity.getId(),
                entity.getCharacter().getId(),
                entity.getTemplate().getId(),
                entity.getPosition(),
                entity.getLevel()
        );
    }

    @Override
    protected String getDeleteQuery(Spell entity) {
        return "DELETE FROM `spell_characters` WHERE `id`='" + entity.getId() + "';";
    }

    @Override
    protected String getSaveQuery(Spell entity) {
        return StringUtils.format(
                "UPDATE `spell_characters` SET " +
                        "`position`='{0}', `level`='{1}'" +
                        " WHERE `id`='{2}';",

                entity.getPosition(),
                entity.getLevel(),

                entity.getId()
        );
    }

    @Override
    protected String getLoadQuery() {
        return "SELECT * FROM `spell_characters`;";
    }

    @Override
    protected String getLoadOneQuery(Long id) {
        return "SELECT * FROM `spell_characters` WHERE `id`='" + id + "';";
    }

    @Override
    protected Spell loadOne(ResultSet reader) throws SQLException {
        Spell spell = new Spell(
            reader.getInt("id"),
            characters.findById(reader.getLong("character")),
            spellTemplates.findById(reader.getInt("spell")),
            reader.getByte("position"),
            reader.getShort("level")
        );

        spell.getCharacter().getSpells().put(spell.getTemplate().getId(), spell);

        return spell;
    }

    @Override
    protected void afterLoading() {
        super.afterLoading();

        for (Spell spell : entities.values()){
            if (spell.getId() > nextId){
                nextId = spell.getId();
            }
        }
    }
}
