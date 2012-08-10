package org.d2j.common.client.protocol.enums;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 13/01/12
 * Time: 19:16
 * To change this template use File | Settings | File Templates.
 */
public enum TradeTypeEnum {
    NPC(0),
    PLAYER(1),
    STORE(4),
    STORE_MANAGEMENT(6),
    COLLECTOR(8),
    MARKET_PLACE_BUY(10),
    MARKET_PLACE_SELL(11);

    private int value;

    TradeTypeEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public boolean superiorThan(TradeTypeEnum e) {
        return this.value > e.value;
    }

    public boolean superiorOrEquals(TradeTypeEnum e) {
        return this.value >= e.value;
    }

    public boolean inferiorThan(TradeTypeEnum e) {
        return this.value < e.value;
    }

    public boolean inferiorOrEquals(TradeTypeEnum e) {
        return this.value <= e.value;
    }

    public boolean equals(TradeTypeEnum e) {
        return this.value == e.value;
    }

    private static HashMap<Integer, TradeTypeEnum> values = new HashMap<>();

    static {
        for (TradeTypeEnum e : values()) {
            values.put(e.value, e);
        }
    }

    public static TradeTypeEnum valueOf(int value) {
        return values.get(value);
    }
}
