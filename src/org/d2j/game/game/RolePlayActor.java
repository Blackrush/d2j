package org.d2j.game.game;

import org.d2j.common.client.protocol.enums.OrientationEnum;
import org.d2j.common.client.protocol.type.BaseRolePlayActorType;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 17/02/12
 * Time: 16:14
 * To change this template use File | Settings | File Templates.
 */
public interface RolePlayActor {
    long getPublicId();

    short getCurrentCell();
    OrientationEnum getCurrentOrientation();

    BaseRolePlayActorType toBaseRolePlayActorType();
}
