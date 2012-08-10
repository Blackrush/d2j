package org.d2j.game.repository;

import org.d2j.common.StringUtils;
import org.d2j.game.game.Cell;
import org.d2j.game.game.pathfinding.Pathfinding;
import org.d2j.game.model.Map;
import org.d2j.utils.database.EntitiesContext;
import org.d2j.utils.database.annotation.Static;
import org.d2j.utils.database.repository.AbstractBaseEntityRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * User: Blackrush
 * Date: 12/11/11
 * Time: 17:01
 * IDE : IntelliJ IDEA
 */
@Singleton
public class MapRepository extends AbstractBaseEntityRepository<Map, Integer> {
    public static Cell[] decompress(int width, String data){
        Cell[] cells = new Cell[data.length() / 10];

        for (short i = 0; i < data.length(); i += 10){
            cells[i / 10] = decompressCell((short) (i / 10), width, data.substring(i, i + 10));
        }

        return cells;
    }

    public static Cell decompressCell(short id, int width, String data){
        int[] hashCodes = new int[10];
        for (int i = 0; i < 10; ++i){
            hashCodes[i] = StringUtils.HASH.indexOf(data.charAt(i));
        }

        boolean los = (hashCodes[0] & 1) == 1;
        int groundLevel = hashCodes[1] & 15;
        int movementType = (hashCodes[2] & 56) >> 3;
        int groundSlope = (hashCodes[4] & 60) >> 2;

        /*
        boolean isInteractive = (hashCodes[7] & 2) >> 1 == 1;
        int interactiveObject = !isInteractive ? 0 : ((hashCodes[0] & 2) << 12) + ((hashCodes[7] & 1) << 12) + (hashCodes[8] << 6) + hashCodes[9];
        */

        return new Cell(
                id,
                los,
                Cell.MovementType.valueOf(movementType),
                groundLevel,
                groundSlope,
                Pathfinding.position(id, width)
        );
    }

    @Inject
    public MapRepository(@Static EntitiesContext context) {
        super(context);
    }

    @Override
    protected String getLoadQuery() {
        return "SELECT * FROM `map_templates`;";
    }

    @Override
    protected String getLoadOneQuery(Integer id) {
        return "SELECT * FROM `map_templates` WHERE `id`='" + id + "';";
    }

    @Override
    protected Map loadOne(ResultSet reader) throws SQLException {
        String data = reader.getString("data");
        byte width = reader.getByte("width");

        Map map = new Map(
                reader.getInt("id"),
                reader.getByte("abscissa"),
                reader.getByte("ordinate"),
                width,
                reader.getByte("height"),
                reader.getShort("subarea"),
                reader.getString("key"),
                reader.getString("date"),
                reader.getBoolean("subscriberArea"),
                reader.getString("places"),
                reader.getByte("maxStores"),
                decompress(width, data)
        );

        return map;
    }
}
