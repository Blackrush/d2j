package org.d2j.game.game;

import org.d2j.common.client.protocol.type.BaseRolePlayActorType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 17/02/12
 * Time: 16:35
 * To change this template use File | Settings | File Templates.
 */
public class RolePlayActors {
    public static Collection<BaseRolePlayActorType> toBaseRolePlayActorType(Collection<RolePlayActor> actors){
        List<BaseRolePlayActorType> result = new ArrayList<>(actors.size());

        for (RolePlayActor actor : actors){
            result.add(actor.toBaseRolePlayActorType());
        }

        return result;
    }
}
