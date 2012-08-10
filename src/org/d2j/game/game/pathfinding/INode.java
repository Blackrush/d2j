package org.d2j.game.game.pathfinding;

import org.d2j.common.client.protocol.enums.OrientationEnum;
import org.d2j.utils.Point;

/**
 * Created by IntelliJ IDEA.
 * User: Blackrush
 * Date: 06/03/12
 * Time: 16:24
 */
public interface INode {
    short getId();
    INode getParent();
    OrientationEnum getOrientation();
    Point getPosition();
    boolean isWalkable();

    int getF();
    int getG();
    int getH();
    void setG(int g);
    void setH(int h);
}
