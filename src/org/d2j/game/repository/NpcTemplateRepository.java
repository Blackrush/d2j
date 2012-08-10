package org.d2j.game.repository;

import org.d2j.common.StringUtils;
import org.d2j.common.client.protocol.enums.NpcTypeEnum;
import org.d2j.game.game.npcs.SellList;
import org.d2j.game.model.NpcQuestion;
import org.d2j.game.model.NpcSell;
import org.d2j.game.model.NpcTemplate;
import org.d2j.utils.database.EntitiesContext;
import org.d2j.utils.database.LoadingException;
import org.d2j.utils.database.annotation.Static;
import org.d2j.utils.database.repository.AbstractBaseEntityRepository;
import org.d2j.utils.database.repository.IBaseEntityRepository;
import org.d2j.utils.database.repository.IEntityRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 26/01/12
 * Time: 18:43
 * To change this template use File | Settings | File Templates.
 */
@Singleton
public class NpcTemplateRepository extends AbstractBaseEntityRepository<NpcTemplate, Integer> {
    private final IBaseEntityRepository<NpcQuestion, Integer> questions;
    private final IEntityRepository<NpcSell, Integer> sells;

    @Inject
    protected NpcTemplateRepository(@Static EntitiesContext context, IBaseEntityRepository<NpcQuestion, Integer> questions, IEntityRepository<NpcSell, Integer> sells) {
        super(context);
        this.questions = questions;
        this.sells = sells;
    }

    @Override
    protected void beforeLoading() throws LoadingException {
        super.beforeLoading();

        if (!questions.isLoaded()){
            throw new LoadingException("NpcQuestionRepository isn't loaded.");
        }
    }

    @Override
    protected String getLoadQuery() {
        return "SELECT * FROM `npc_templates`;";
    }

    @Override
    protected String getLoadOneQuery(Integer id) {
        return "SELECT * FROM `npc_templates` WHERE `id`='" + id + "';";
    }

    private static Collection<Integer> stringToAccessories(String string){
        String[] accessories = string.split(",");
        List<Integer> result = new ArrayList<>(accessories.length);
        for (String accessory : accessories){
            result.add(StringUtils.fromHex(accessory));
        }
        return result;
    }

    @Override
    protected NpcTemplate loadOne(ResultSet reader) throws SQLException {
        NpcTemplate npc = new NpcTemplate(
                reader.getInt("id"),
                NpcTypeEnum.valueOf(reader.getInt("type")),
                reader.getShort("skin"),
                reader.getShort("scaleX"),
                reader.getShort("scaleY"),
                reader.getBoolean("gender"),
                reader.getInt("color1"),
                reader.getInt("color2"),
                reader.getInt("color3"),
                questions.findById(reader.getInt("question")),
                stringToAccessories(reader.getString("accessories")),
                reader.getInt("extraClip"),
                reader.getInt("customArtwork")
        );

        npc.setSells(new SellList(npc, sells));

        return npc;
    }
}
