package org.d2j.game.repository;

import org.d2j.common.StringUtils;
import org.d2j.game.configuration.IGameConfiguration;
import org.d2j.game.model.*;
import org.d2j.game.model.Character;
import org.d2j.utils.database.EntitiesContext;
import org.d2j.utils.database.LoadingException;
import org.d2j.utils.database.annotation.Dynamic;
import org.d2j.utils.database.repository.AbstractEntityRepository;
import org.d2j.utils.database.repository.IBaseEntityRepository;
import org.d2j.utils.database.repository.IEntityRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 22/02/12
 * Time: 19:21
 * To change this template use File | Settings | File Templates.
 */
@Singleton
public class WaypointRecordRepository extends AbstractEntityRepository<WaypointRecord, Long> {
    private final IGameConfiguration config;
    private final IBaseEntityRepository<Waypoint, Short> waypoints;
    private final IEntityRepository<Character, Long> characters;

    private long nextId;

    @Inject
    protected WaypointRecordRepository(@Dynamic EntitiesContext context, IGameConfiguration config, IBaseEntityRepository<Waypoint, Short> waypoints, IEntityRepository<Character, Long> characters) {
        super(context);
        this.config = config;
        this.waypoints = waypoints;
        this.characters = characters;
    }

    @Override
    protected void beforeLoading() throws LoadingException {
        super.beforeLoading();

        if (!waypoints.isLoaded()){
            throw new LoadingException("WaypointRepository is not loaded.");
        }
        if (!characters.isLoaded()){
            throw new LoadingException("CharacterRepository is not loaded.");
        }
    }

    @Override
    protected void afterLoading() {
        super.afterLoading();

        for (WaypointRecord record : entities.values()){
            if (nextId < record.getId()){
                nextId = record.getId();
            }
        }
    }

    @Override
    protected void setNextId(WaypointRecord entity) {
        entity.setId(++nextId);
    }

    @Override
    public long loadAll() throws LoadingException {
        if (config.getAddAllWaypoints()){
            for (Character character : characters){
                for (Waypoint waypoint : waypoints){
                    character.getWaypoints().addEntity(new WaypointRecord(-1, character, waypoint));
                }
            }

            return 0;
        }
        else{
            return super.loadAll();
        }
    }

    @Override
    public void create(WaypointRecord entity) {
        if (!config.getAddAllWaypoints()){
            super.create(entity);
        }
    }

    @Override
    public void delete(WaypointRecord entity) {
        if (!config.getAddAllWaypoints()){
            super.delete(entity);
        }
    }

    @Override
    public void save(WaypointRecord entity) {
        if (!config.getAddAllWaypoints()){
            super.save(entity);
        }
    }

    @Override
    protected String getCreateQuery(WaypointRecord entity) {
        return StringUtils.format("INSERT INTO `waypoints`(`id`,`character`,`zaap`) VALUES('{0}','{1}','{2}');",
                entity.getId(),
                entity.getCharacter().getId(),
                entity.getWaypoint().getId()
        );
    }

    @Override
    protected String getDeleteQuery(WaypointRecord entity) {
        return StringUtils.format("DELETE FROM `waypoints` WHERE `id`='{0}';", entity.getId());
    }

    @Override
    protected String getSaveQuery(WaypointRecord entity) {
        return null;
    }

    @Override
    protected String getLoadQuery() {
        return "SELECT * FROM `waypoints`;";
    }

    @Override
    protected String getLoadOneQuery(Long id) {
        return "SELECT * FROM `waypoints` WHERE `id`='" + id + "';";
    }

    @Override
    protected WaypointRecord loadOne(ResultSet reader) throws SQLException {
        Character character = characters.findById(reader.getLong("character"));
        WaypointRecord record = new WaypointRecord(
                reader.getLong("id"),
                character,
                waypoints.findById(reader.getShort("zaap"))
        );
        character.getWaypoints().addEntity(record);
        return record;
    }
}
