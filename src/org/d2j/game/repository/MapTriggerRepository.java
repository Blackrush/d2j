package org.d2j.game.repository;

import org.d2j.game.model.Map;
import org.d2j.game.model.MapTrigger;
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
 * User: Blackrush
 * Date: 12/11/11
 * Time: 17:14
 * IDE : IntelliJ IDEA
 */
@Singleton
public class MapTriggerRepository extends AbstractBaseEntityRepository<MapTrigger, Integer> {
    private final IBaseEntityRepository<Map, Integer> maps;

    @Inject
    public MapTriggerRepository(@Static EntitiesContext context, IBaseEntityRepository<Map, Integer> maps) {
        super(context);

        this.maps = maps;
    }

    @Override
    protected String getLoadQuery() {
        return "SELECT * FROM `map_triggers`;";
    }

    @Override
    protected String getLoadOneQuery(Integer id) {
        return "SELECT * FROM `map_triggers` WHERE `id`='" + id + "';";
    }

    @Override
    protected MapTrigger loadOne(ResultSet reader) throws SQLException {
        Map map     = maps.findById(reader.getInt("map")),
            nextMap = maps.findById(reader.getInt("nextMap"));
        if (map == null || nextMap == null) return null;

        MapTrigger trigger = new MapTrigger(
                reader.getInt("id"),
                map,
                reader.getShort("cell"),
                nextMap,
                reader.getShort("nextCell")
        );

        map.getTriggers().put(trigger.getCellId(), trigger);

        return trigger;
    }

    @Override
    protected void beforeLoading() throws LoadingException {
        if (!maps.isLoaded()) throw new LoadingException("MapRepository must be loaded.");
    }
}
