package org.d2j.common.client.protocol.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 30/01/12
 * Time: 20:03
 * To change this template use File | Settings | File Templates.
 */
public enum NpcTypeEnum {
    BUY_SELL(1),
    TRADE(2),
    SPEAK(3),
    PET(4),
    SELL(5),
    BUY(6),
    RESURECT_PET(7),
    MOUNT(8);

    private int value;

    private NpcTypeEnum(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    private static final Map<Integer, NpcTypeEnum> values = new HashMap<>();

    static {
        for (NpcTypeEnum e : values()) {
            values.put(e.value(), e);
        }
    }

    public static NpcTypeEnum valueOf(int value) {
        return values.get(value);
    }
}
