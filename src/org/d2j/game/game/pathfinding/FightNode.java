package org.d2j.game.game.pathfinding;

import org.d2j.common.client.protocol.enums.OrientationEnum;
import org.d2j.game.game.fights.FightCell;
import org.d2j.utils.Point;

/**
 * Created by IntelliJ IDEA.
 * User: Blackrush
 * Date: 06/03/12
 * Time: 15:58
 */
public class FightNode implements INode {
    private FightCell cell;
    private FightNode parent;
    private OrientationEnum orientation;
    private int g, h;

    public FightNode(FightCell cell, OrientationEnum orientation) {
        this.cell = cell;
        this.orientation = orientation;
    }

    public FightNode(FightCell cell, FightNode parent, OrientationEnum orientation) {
        this.cell = cell;
        this.parent = parent;
        this.orientation = orientation;
    }

    @Override
    public short getId() {
        return cell.getId();
    }

    @Override
    public FightNode getParent() {
        return parent;
    }

    @Override
    public OrientationEnum getOrientation() {
        return orientation;
    }

    @Override
    public Point getPosition() {
        return cell.getPosition();
    }

    @Override
    public boolean isWalkable() {
        return cell.isWalkable();
    }

    public FightCell getCell(){
        return cell;
    }

    @Override
    public int getF() {
        return g + h;
    }

    @Override
    public int getG() {
        return g;
    }

    @Override
    public int getH() {
        return h;
    }

    @Override
    public void setG(int g) {
        this.g = g;
    }

    @Override
    public void setH(int h) {
        this.h = h;
    }
}
