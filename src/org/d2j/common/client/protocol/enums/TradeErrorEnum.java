package org.d2j.common.client.protocol.enums;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 13/01/12
 * Time: 19:22
 * To change this template use File | Settings | File Templates.
 */
public enum TradeErrorEnum {
    UnknownOrOffline('E'),
    Busy('O');

    private char value;

    TradeErrorEnum(char value) {
        this.value = value;
    }

    public char value() {
        return value;
    }

    public boolean equals(TradeErrorEnum e) {
        return this.value == e.value;
    }

    private static HashMap<Character, TradeErrorEnum> values = new HashMap<>();

    static {
        for (TradeErrorEnum e : values()) {
            values.put(e.value, e);
        }
    }

    public static TradeErrorEnum valueOf(char value) {
        return values.get(value);
    }
}
