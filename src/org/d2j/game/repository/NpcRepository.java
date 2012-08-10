package org.d2j.game.repository;

import org.d2j.common.StringUtils;
import org.d2j.common.client.protocol.enums.OrientationEnum;
import org.d2j.game.model.Map;
import org.d2j.game.model.Npc;
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
 * Date: 26/01/12
 * Time: 19:21
 * To change this template use File | Settings | File Templates.
 */
@Singleton
public class NpcRepository extends AbstractEntityRepository<Npc, Integer> {
    public static Npc findByTemplate(IBaseEntityRepository<Npc, Integer> repo, NpcTemplate template){
        for (Npc npc : repo.all()){
            if (npc.getTemplate() == template){
                return npc;
            }
        }
        return null;
    }
    public static Npc findByTemplateId(IBaseEntityRepository<Npc, Integer> repo, int templateId){
        for (Npc npc : repo.all()){
            if (npc.getTemplate().getId() == templateId){
                return npc;
            }
        }
        return null;
    }

    private final IBaseEntityRepository<NpcTemplate, Integer> npcTemplates;
    private final IBaseEntityRepository<Map, Integer> maps;
    private int nextId;

    @Inject
    protected NpcRepository(@Dynamic EntitiesContext context, IBaseEntityRepository<NpcTemplate, Integer> npcTemplates, IBaseEntityRepository<Map, Integer> maps) {
        super(context);
        this.npcTemplates = npcTemplates;
        this.maps = maps;
    }

    public Npc findByTemplate(NpcTemplate template){
        return findByTemplate(this, template);
    }

    public Npc findByTemplateId(int templateId){
        return findByTemplateId(this, templateId);
    }

    @Override
    protected void setNextId(Npc entity) {
        entity.setId(++nextId);
    }

    @Override
    protected void beforeLoading() throws LoadingException {
        super.beforeLoading();

        if (!npcTemplates.isLoaded()){
            throw new LoadingException("NpcTemplateRepository must be loaded.");
        }

        if (!maps.isLoaded()){
            throw new LoadingException("MapRepository must be loaded.");
        }
    }

    @Override
    protected void afterLoading() {
        super.afterLoading();

        for (Npc npc : entities.values()){
            if (nextId < npc.getId()){
                nextId = npc.getId();
            }
        }
    }

    @Override
    public void create(Npc entity) {
        super.create(entity);

        entity.setPublicId(entity.getMap().getNextPublicId());
        entity.getMap().addActor(entity);
    }

    @Override
    public void delete(Npc entity) {
        entity.getMap().removeActor(entity);

        super.delete(entity);
    }

    @Override
    protected String getCreateQuery(Npc entity) {
        return StringUtils.format(
                "INSERT INTO `npcs`(`id`,`template`,`map`,`cell`,`orientation`) VALUES('{0}','{1}','{2}','{3}','{4}');",
                entity.getId(),
                entity.getTemplate().getId(),
                entity.getMap().getId(),
                entity.getCurrentCell(),
                entity.getCurrentOrientation().ordinal()
        );
    }

    @Override
    protected String getDeleteQuery(Npc entity) {
        return "DELETE FROM `npcs` WHERE `id`='" + entity.getId() + "';";
    }

    @Override
    protected String getSaveQuery(Npc entity) {
        return StringUtils.format(
                "UPDATE `npcs` SET `template`='{0}', `map`='{1}', `cell`='{2}', `orientation`='{3}' WHERE `id`='{4}';",
                entity.getTemplate().getId(),
                entity.getMap().getId(),
                entity.getCurrentCell(),
                entity.getCurrentOrientation().ordinal(),

                entity.getId()
        );
    }

    @Override
    protected String getLoadQuery() {
        return "SELECT * FROM `npcs`;";
    }

    @Override
    protected String getLoadOneQuery(Integer id) {
        return "SELECT * FROM `npcs` WHERE `id`='" + id + "';";
    }

    @Override
    protected Npc loadOne(ResultSet reader) throws SQLException {
        Npc npc = new Npc(
                reader.getInt("id"),
                npcTemplates.findById(reader.getInt("template")),
                maps.findById(reader.getInt("map")),
                reader.getShort("cell"),
                OrientationEnum.valueOf(reader.getInt("orientation"))
        );

        npc.setPublicId(npc.getMap().getNextPublicId());
        npc.getMap().addActor(npc);

        return npc;
    }
}
