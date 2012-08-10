package org.d2j.game.game.friends;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: blackrush
 * Date: 31/12/11
 * Time: 13:01
 * To change this template use File | Settings | File Templates.
 */
public enum FriendType {
    FRIEND,
    ENNEMY;

    public boolean superiorThan(FriendType o) {
        return compare(this, o) > 0;
    }

    public boolean inferiorThan(FriendType o) {
        return compare(this, o) < 0;
    }

    public boolean equals(FriendType o) {
        return compare(this, o) == 0;
    }

    private static HashMap<Integer, FriendType> values = new HashMap<>();

    static {
        for (FriendType value : values()) {
            values.put(value.ordinal(), value);
        }
    }

    public static FriendType valueOf(int value) {
        return values.get(value);
    }

    public static int compare(FriendType o1, FriendType o2) {
        return o1.ordinal() - o2.ordinal();
    }
}
