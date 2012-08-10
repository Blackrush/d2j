package org.d2j.common.client.protocol.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Blackrush
 * Date: 15/11/11
 * Time: 19:25
 * IDE : IntelliJ IDEA
 */
public enum FightTypeEnum {
    CHALLENGE,
    AGRESSION,
    UNKNOWN0,
    UNKNOWN1,
    MONSTER,
    TAX_COLLECTOR;

    private static final Map<Integer, FightTypeEnum> values = new HashMap<>();
    static{
        for (FightTypeEnum type : values())
            values.put(type.ordinal(), type);
    }

    public static FightTypeEnum valueOf(Integer ordinal){
        return values.get(ordinal);
    }
}
