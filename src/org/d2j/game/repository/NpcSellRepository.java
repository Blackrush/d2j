package org.d2j.game.repository;

import org.d2j.common.StringUtils;
import org.d2j.game.model.ItemTemplate;
import org.d2j.game.model.NpcSell;
import org.d2j.game.model.NpcTemplate;
import org.d2j.utils.database.EntitiesContext;
import org.d2j.utils.database.LoadingException;
import org.d2j.utils.database.annotation.Dynamic;
import org.d2j.utils.database.repository.AbstractEntityRepository;
import org.d2j.utils.database.repository.IBaseEntityRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 01/02/12
 * Time: 19:32
 * To change this template use File | Settings | File Templates.
 */
@Singleton
public class NpcSellRepository extends AbstractEntityRepository<NpcSell, Integer> {
    private int nextId;

    private final IBaseEntityRepository<NpcTemplate, Integer> npcTemplates;
    private final IBaseEntityRepository<ItemTemplate, Integer> itemTemplates;

    @Inject
    protected NpcSellRepository(@Dynamic EntitiesContext context, IBaseEntityRepository<NpcTemplate, Integer> npcTemplates, IBaseEntityRepository<ItemTemplate, Integer> itemTemplates) {
        super(context);
        this.npcTemplates = npcTemplates;
        this.itemTemplates = itemTemplates;
    }

    @Override
    protected void beforeLoading() throws LoadingException {
        super.beforeLoading();

        if (!npcTemplates.isLoaded()){
            throw new LoadingException("NpcTemplateRepository is not loaded.");
        }

        if (!itemTemplates.isLoaded()){
            throw new LoadingException("ItemTemplateRepository is not loaded.");
        }
    }

    @Override
    protected void afterLoading() {
        super.afterLoading();

        for (NpcSell sell : entities.values()){
            if (nextId < sell.getId()){
                nextId = sell.getId();
            }
        }
    }

    @Override
    protected void setNextId(NpcSell entity) {
        entity.setId(++nextId);
    }

    @Override
    protected NpcSell loadOne(ResultSet reader) throws SQLException {
        NpcSell sell = new NpcSell(
                reader.getInt("id"),
                npcTemplates.findById(reader.getInt("npc")),
                itemTemplates.findById(reader.getInt("item"))
        );

        sell.getNpc().getSells().addData(sell);

        return sell;
    }

    @Override
    protected String getCreateQuery(NpcSell entity) {
        return StringUtils.format(
                "INSERT INTO `npc_sells`(`id`,`npc`,`item`) VALUES('{0}','{1}','{2}');",
                entity.getId(),
                entity.getNpc().getId(),
                entity.getItem().getId()
        );
    }

    @Override
    protected String getDeleteQuery(NpcSell entity) {
        return "DELETE FROM `npc_sells` WHERE `id`='" + entity.getId() + "';";
    }

    @Override
    protected String getSaveQuery(NpcSell entity) {
        return StringUtils.format(
                "UPDATE `npc_sells` SET `npc`='{0}', `item`='{1}' WHERE `id`='{2}';",
                entity.getNpc().getId(),
                entity.getItem().getId(),
                entity.getId()
        );
    }

    @Override
    protected String getLoadQuery() {
        return "SELECT * FROM `npc_sells`;";
    }

    @Override
    protected String getLoadOneQuery(Integer id) {
        return "SELECT * FROM `npc_sells` WHERE `id`='" + id + "';";
    }
}
