package org.d2j.game.game.guilds;

import org.d2j.common.client.protocol.enums.GuildMemberRightsEnum;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 07/02/12
 * Time: 17:55
 * To change this template use File | Settings | File Templates.
 */
public class RightList {
    private int rights;

    public RightList() {
    }

    public RightList(int rights) {
        this.rights = rights;
    }

    public RightList fill(boolean value){
        rights = value ? Integer.MAX_VALUE : Integer.MIN_VALUE;
        return this;
    }

    public boolean get(GuildMemberRightsEnum right){
        return right != null && (rights & right.value()) != 0;
    }

    public void set(GuildMemberRightsEnum right, boolean value){
        if (right != null){
            if (value){
                rights |= right.value();
            }
            else{
                rights ^= right.value();
            }
        }
    }

    public int toInt(){
        return rights;
    }

    public void fromInt(int rights){
        this.rights = rights;
    }
}
