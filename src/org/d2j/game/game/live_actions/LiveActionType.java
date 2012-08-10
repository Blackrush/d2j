package org.d2j.game.game.live_actions;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 30/01/12
 * Time: 20:16
 * To change this template use File | Settings | File Templates.
 */
public enum LiveActionType {
    NONE(0),
    RESTAT(1),
    CREATE_GUILD(2),
    SHOW_MESSAGE(3),
    CONTINUE_QUESTION(4),
    TELEPORT(5),
    TELEPORT_BACK(6),
    ADD_ITEM(7),
    REMOVE_ITEM(8),
    ADD_LIFE(9),
    ADD_KAMAS(10);

    private int value;
    LiveActionType(int value){
        this.value = value;
    }
    public int value(){
        return value;
    }

    private static HashMap<Integer, LiveActionType> values = new HashMap<>();
    static{
        for (LiveActionType liveActionType : values()){
            values.put(liveActionType.value, liveActionType);
        }
    }
    public static LiveActionType valueOf(int value) {
        return values.get(value);
    }
}
