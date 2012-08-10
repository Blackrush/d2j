package org.d2j.common.client.protocol.enums;

/**
 * User: Blackrush
 * Date: 20/11/11
 * Time: 12:06
 * IDE : IntelliJ IDEA
 */

import java.util.HashMap;
import java.util.Map;

public enum FightStateEnum {
    UNKNOWN,
    INIT,
    PLACE,
    ACTIVE,
    FINISHED;

    private static final Map<Integer, FightStateEnum> values = new HashMap<>();

    static {
        for (FightStateEnum e : values()) {
            values.put(e.ordinal(), e);
        }
    }

    public static FightStateEnum valueOf(int ordinal) {
        return values.get(ordinal);
    }
}
