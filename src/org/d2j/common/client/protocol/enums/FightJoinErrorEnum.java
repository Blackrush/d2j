package org.d2j.common.client.protocol.enums;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 19/02/12
 * Time: 15:51
 * To change this template use File | Settings | File Templates.
 */
public enum FightJoinErrorEnum {
    UNAVAILABLE('t'),
    DENIED('f');

    private char value;
    FightJoinErrorEnum(char value) {
        this.value = value;
    }
    public char value() {
        return value;
    }
    public boolean equals(FightJoinErrorEnum e) {
        return this.value == e.value;
    }
    public String toString(){
        return String.valueOf(value);
    }

    private static HashMap<Character, FightJoinErrorEnum> values = new HashMap<>();
    static {
        for (FightJoinErrorEnum e : values()) {
            values.put(e.value, e);
        }
    }
    public static FightJoinErrorEnum valueOf(char value) {
        return values.get(value);
    }
}
