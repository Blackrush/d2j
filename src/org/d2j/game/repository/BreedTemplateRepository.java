package org.d2j.game.repository;

import org.d2j.game.model.BreedTemplate;
import org.d2j.utils.database.EntitiesContext;
import org.d2j.utils.database.annotation.Static;
import org.d2j.utils.database.repository.AbstractBaseEntityRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * User: Blackrush
 * Date: 02/11/11
 * Time: 14:26
 * IDE : IntelliJ IDEA
 */
@Singleton
public class BreedTemplateRepository extends AbstractBaseEntityRepository<BreedTemplate, Byte> {
    @Inject
    public BreedTemplateRepository(@Static EntitiesContext context) {
        super(context);
    }

    @Override
    protected String getLoadQuery() {
        return "SELECT * FROM `breed_templates`;";
    }

    @Override
    protected String getLoadOneQuery(Byte id) {
        return "SELECT * FROM `breed_templates` WHERE `id`='" + id + "';";
    }

    @Override
    protected BreedTemplate loadOne(ResultSet reader) throws SQLException {
        return new BreedTemplate(
                reader.getByte("id"),
                reader.getShort("startAP"),
                reader.getShort("startMP"),
                reader.getShort("startLife"),
                reader.getShort("startProspection"),
                reader.getString("intelligence"),
                reader.getString("chance"),
                reader.getString("agility"),
                reader.getString("strength"),
                reader.getString("vitality"),
                reader.getString("wisdom")
        );
    }
}
