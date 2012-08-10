package org.d2j.common.client.protocol.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 19/02/12
 * Time: 15:06
 * To change this template use File | Settings | File Templates.
 */
public enum FightAttributeType {
    NEED_HELP('H'),
    DENY_ALL('N'),
    ALLOW_PARTY('P'),
    DENY_SPECTATORS('S');

    private char value;
    FightAttributeType(char value) {
        this.value = value;
    }
    public char value() {
        return value;
    }
    public boolean equals(FightAttributeType e) {
        return this.value == e.value;
    }
    public String toString(){
        return String.valueOf(value);
    }

    private static HashMap<Character, FightAttributeType> values = new HashMap<>();
    static {
        for (FightAttributeType e : values()) {
            values.put(e.value, e);
        }
    }
    public static FightAttributeType valueOf(char value) {
        return values.get(value);
    }
    public static Map<FightAttributeType, Boolean> emptyMap(boolean defaultz){
        Map<FightAttributeType, Boolean> result = new HashMap<>(values().length);
        for (FightAttributeType attribute : values()){
            result.put(attribute, defaultz);
        }
        return result;
    }
}
