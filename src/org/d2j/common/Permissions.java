package org.d2j.common;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Blackrush
 * Date: 29/10/11
 * Time: 19:06
 * IDE : IntelliJ IDEA
 */
public enum Permissions {
    BANNED(0),
    MEMBER(1),
    QUIZ_MASTER(2),
    GAME_MASTER(3),
    ADMINISTRATOR(4);

    private int value;
    Permissions(int value){
        this.value = value;
    }
    public int value(){
        return value;
    }
    public boolean superior(Permissions permissions){
        return this.value > permissions.value;
    }
    public boolean inferior(Permissions permissions){
        return this.value < permissions.value;
    }
    public boolean equals(Permissions permissions){
        return this.value == permissions.value;
    }
    public boolean superiorOrEquals(Permissions permissions){
        return this.value >= permissions.value;
    }
    public boolean inferiorOrEquals(Permissions permissions){
        return this.value <= permissions.value;
    }

    private static Map<Integer, Permissions> values = new HashMap<Integer, Permissions>();

    static{
        for (Permissions permissions : values()){
            values.put(permissions.value, permissions);
        }
    }

    public static Permissions valueOf(Integer ordinal){
        return values.get(ordinal);
    }
}
