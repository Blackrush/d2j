package org.d2j.game.repository;

import org.d2j.game.model.MarketPlace;
import org.d2j.game.model.Npc;
import org.d2j.utils.database.EntitiesContext;
import org.d2j.utils.database.LoadingException;
import org.d2j.utils.database.annotation.Static;
import org.d2j.utils.database.repository.AbstractBaseEntityRepository;
import org.d2j.utils.database.repository.IEntityRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 27/02/12
 * Time: 10:10
 * To change this template use File | Settings | File Templates.
 */
@Singleton
public class MarketPlaceRepository extends AbstractBaseEntityRepository<MarketPlace, Short> {
    private final IEntityRepository<Npc, Integer> npcs;

    @Inject
    protected MarketPlaceRepository(@Static EntitiesContext context, IEntityRepository<Npc, Integer> npcs) {
        super(context);
        this.npcs = npcs;
    }

    @Override
    protected String getLoadQuery() {
        return "SELECT * FROM `marketplaces`;";
    }

    @Override
    protected String getLoadOneQuery(Short id) {
        return "SELECT * FROM `marcketplaces` WHERE `id`='" + id + "';";
    }

    @Override
    protected MarketPlace loadOne(ResultSet reader) throws SQLException {
        MarketPlace marketPlace = new MarketPlace(
                reader.getShort("id"),
                NpcRepository.findByTemplateId(npcs, reader.getInt("npc"))
        );

        if (marketPlace.getMap().getMarketPlace() != null) {
            throw new LoadingException("There is already a marketplace on this map");
        }
        else{
            marketPlace.getMap().setMarketPlace(marketPlace);
        }

        return marketPlace;
    }
}
