package org.d2j.game.game.fights;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Blackrush
 * Date: 16/11/11
 * Time: 09:35
 * IDE : IntelliJ IDEA
 */
public enum FightTeamEnum {
    CHALLENGER,
    DEFENDER,
    SPECTATOR;

    private static final Map<Integer, FightTeamEnum> values = new HashMap<>();
    static{
        for (FightTeamEnum team : values()){
            values.put(team.ordinal(), team);
        }
    }

    public static FightTeamEnum valueOf(int ordinal){
        return values.get(ordinal);
    }
}
