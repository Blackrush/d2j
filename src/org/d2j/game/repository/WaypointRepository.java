package org.d2j.game.repository;

import org.d2j.game.model.Map;
import org.d2j.game.model.Waypoint;
import org.d2j.utils.database.EntitiesContext;
import org.d2j.utils.database.LoadingException;
import org.d2j.utils.database.annotation.Static;
import org.d2j.utils.database.repository.AbstractBaseEntityRepository;
import org.d2j.utils.database.repository.IBaseEntityRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 22/02/12
 * Time: 19:13
 * To change this template use File | Settings | File Templates.
 */
@Singleton
public class WaypointRepository extends AbstractBaseEntityRepository<Waypoint, Short> {
    private final IBaseEntityRepository<Map, Integer> maps;

    @Inject
    protected WaypointRepository(@Static EntitiesContext context, IBaseEntityRepository<Map, Integer> maps) {
        super(context);
        this.maps = maps;
    }

    public Waypoint findByMap(Map map){
        for (Waypoint waypoint : entities.values()){
            if (waypoint.getMap() == map){
                return waypoint;
            }
        }
        return null;
    }

    public Waypoint findByMapId(int mapId){
        return findByMap(maps.findById(mapId));
    }

    @Override
    protected void beforeLoading() throws LoadingException {
        super.beforeLoading();

        if (!maps.isLoaded()){
            throw new LoadingException("MapRepository is not loaded.");
        }
    }

    @Override
    protected String getLoadQuery() {
        return "SELECT * FROM `waypoint_templates`;";
    }

    @Override
    protected String getLoadOneQuery(Short id) {
        return "SELECT * FROM `waypoint_templates` WHERE `id`='" + id + "';";
    }

    @Override
    protected Waypoint loadOne(ResultSet reader) throws SQLException {
        Map map = maps.findById(reader.getInt("map"));
        Waypoint waypoint = new Waypoint(
                reader.getShort("id"),
                map,
                reader.getShort("cell")
        );
        map.setWaypoint(waypoint);
        return waypoint;
    }
}
