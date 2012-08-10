package org.d2j.game.repository;

import org.d2j.game.game.live_actions.LazyLoadLiveAction;
import org.d2j.game.game.live_actions.LiveActionType;
import org.d2j.game.model.NpcResponse;
import org.d2j.utils.database.EntitiesContext;
import org.d2j.utils.database.LazyLoadEntity;
import org.d2j.utils.database.annotation.Static;
import org.d2j.utils.database.repository.AbstractBaseEntityRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 30/01/12
 * Time: 20:19
 * To change this template use File | Settings | File Templates.
 */
@Singleton
public class NpcResponseRepository extends AbstractBaseEntityRepository<NpcResponse, Integer> {
    @Inject
    protected NpcResponseRepository(@Static EntitiesContext context) {
        super(context);
    }

    @Override
    protected String getLoadQuery() {
        return "SELECT * FROM `npc_responses`;";
    }

    @Override
    protected String getLoadOneQuery(Integer id) {
        return "SELECT * FROM `npc_responses` WHERE `id`='" + id + "';";
    }

    @Override
    protected NpcResponse loadOne(final ResultSet reader) throws SQLException {
        LiveActionType type = LiveActionType.valueOf(reader.getInt("type"));
        int next = reader.getInt("next");

        return new NpcResponse(
                reader.getInt("id"),
                type != LiveActionType.NONE ? new LazyLoadLiveAction(type, reader.getString("arguments").split(";")) : null,
                next > 0 ? new LazyLoadEntity<>(this, next) : null
        );
    }
}
