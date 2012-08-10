package org.d2j.game.repository;

import org.d2j.common.client.protocol.enums.ItemEffectEnum;
import org.d2j.game.game.items.ItemEffect;
import org.d2j.game.model.ItemSetTemplate;
import org.d2j.game.model.ItemTemplate;
import org.d2j.utils.database.EntitiesContext;
import org.d2j.utils.database.annotation.Static;
import org.d2j.utils.database.repository.AbstractBaseEntityRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * User: Blackrush
 * Date: 28/12/11
 * Time: 11:34
 * IDE : IntelliJ IDEA
 */
@Singleton
public class ItemSetTemplateRepository extends AbstractBaseEntityRepository<ItemSetTemplate, Short> {
    @Inject
    protected ItemSetTemplateRepository(@Static EntitiesContext context) {
        super(context);
    }

    @Override
    protected String getLoadQuery() {
        return "SELECT * FROM `itemset_templates`;";
    }

    @Override
    protected String getLoadOneQuery(Short id) {
        return "SELECT * FROM `itemset_templates` WHERE `id`='" + id + "';";
    }

    @Override
    protected ItemSetTemplate loadOne(ResultSet reader) throws SQLException {
        return new ItemSetTemplate(
                reader.getShort("id"),
                reader.getString("name"),
                new ArrayList<ItemTemplate>(),
                Arrays.asList(
                        buildItemEffectCollection(reader.getString("effects2")),
                        buildItemEffectCollection(reader.getString("effects3")),
                        buildItemEffectCollection(reader.getString("effects4")),
                        buildItemEffectCollection(reader.getString("effects5")),
                        buildItemEffectCollection(reader.getString("effects6")),
                        buildItemEffectCollection(reader.getString("effects7")),
                        buildItemEffectCollection(reader.getString("effects8"))
                )
        );
    }

    private Collection<ItemEffect> buildItemEffectCollection(String string){
        if (string.isEmpty()) return new ArrayList<>(0);
        List<ItemEffect> result = new ArrayList<>();
        for (String sEffect : string.split(";")){
            String[] args = sEffect.split(",");

            ItemEffectEnum effect = ItemEffectEnum.valueOf(Integer.parseInt(args[0].trim()));
            short bonus = Short.parseShort(args[1].trim());

            result.add(new ItemEffect(effect, bonus));
        }
        return result;
    }
}
