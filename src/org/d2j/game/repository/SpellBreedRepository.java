package org.d2j.game.repository;

import org.d2j.game.model.BreedTemplate;
import org.d2j.game.model.SpellBreed;
import org.d2j.game.model.SpellTemplate;
import org.d2j.utils.database.EntitiesContext;
import org.d2j.utils.database.LoadingException;
import org.d2j.utils.database.annotation.Static;
import org.d2j.utils.database.repository.AbstractBaseEntityRepository;
import org.d2j.utils.database.repository.IBaseEntityRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;

/**
 * User: Blackrush
 * Date: 08/12/11
 * Time: 13:14
 * IDE : IntelliJ IDEA
 */
@Singleton
public class SpellBreedRepository extends AbstractBaseEntityRepository<SpellBreed, Integer> {
    private final IBaseEntityRepository<BreedTemplate, Byte> breedTemplates;
    private final IBaseEntityRepository<SpellTemplate, Integer> spellTemplates;

    @Inject
    public SpellBreedRepository(@Static EntitiesContext context, IBaseEntityRepository<BreedTemplate, Byte> breedTemplates, IBaseEntityRepository<SpellTemplate, Integer> spellTemplates) {
        super(context);
        this.breedTemplates = breedTemplates;
        this.spellTemplates = spellTemplates;
    }

    @Override
    protected void beforeLoading() throws LoadingException {
        super.beforeLoading();

        if (!breedTemplates.isLoaded()){
            throw new LoadingException("BreedTemplates isn't loaded.");
        }

        if (!spellTemplates.isLoaded()){
            throw new LoadingException("SpellTemplates isn't loaded.");
        }
    }

    @Override
    protected void afterLoading() {
        super.afterLoading();

        for (BreedTemplate breed : breedTemplates.all()){
            Collections.sort(breed.getSpells(), new Comparator<SpellBreed>() {
                @Override
                public int compare(SpellBreed o1, SpellBreed o2) {
                    return o1.getLevel() - o2.getLevel();
                }
            });
        }
    }

    @Override
    protected String getLoadQuery() {
        return "SELECT * FROM `spell_breed_templates`;";
    }

    @Override
    protected String getLoadOneQuery(Integer id) {
        return "SELECT * FROM `spell_breed_templates` WHERE `id`='" + id + "';";
    }

    @Override
    protected SpellBreed loadOne(ResultSet reader) throws SQLException {
        SpellBreed spell = new SpellBreed(
                reader.getInt("id"),
                breedTemplates.findById(reader.getByte("breed")),
                reader.getShort("level"),
                spellTemplates.findById(reader.getInt("spell")),
                reader.getByte("default_position")
        );

        spell.getBreed().getSpells().add(spell);

        return spell;
    }
}
