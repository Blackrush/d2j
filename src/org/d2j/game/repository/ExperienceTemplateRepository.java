package org.d2j.game.repository;

import org.d2j.game.model.ExperienceTemplate;
import org.d2j.utils.database.EntitiesContext;
import org.d2j.utils.database.annotation.Static;
import org.d2j.utils.database.repository.AbstractBaseEntityRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * User: Blackrush
 * Date: 12/11/11
 * Time: 09:37
 * IDE : IntelliJ IDEA
 */
@Singleton
public class ExperienceTemplateRepository extends AbstractBaseEntityRepository<ExperienceTemplate, Short> {
    @Inject
    public ExperienceTemplateRepository(@Static EntitiesContext context) {
        super(context);
    }

    @Override
    protected String getLoadQuery() {
        return "SELECT * FROM `experience_templates`;";
    }

    @Override
    protected String getLoadOneQuery(Short id) {
        return "SELECT * FROM `experience_templates` WHERE `level`='" + id + "';";
    }

    @Override
    protected ExperienceTemplate loadOne(ResultSet reader) throws SQLException {
        return new ExperienceTemplate(
                reader.getShort("level"),
                reader.getLong("character"),
                reader.getInt("job"),
                reader.getInt("mount"),
                reader.getShort("alignment")
        );
    }
}
