package org.d2j.game.game.pathfinding;

import org.d2j.common.client.protocol.enums.OrientationEnum;
import org.d2j.game.game.Cell;
import org.d2j.utils.Point;

/**
 * User: Blackrush
 * Date: 26/11/11
 * Time: 20:19
 * IDE : IntelliJ IDEA
 */
public class Node implements INode {
    private short id;
    private Node parent;
    private OrientationEnum orientation;
    private Point position;
    private boolean walkable;
    private int g, h;

    public Node(short id, Point position) {
        this.id = id;
        this.position = position;
    }

    public Node(short id, Point position, boolean walkable, OrientationEnum orientation) {
        this.id = id;
        this.position = position;
        this.walkable = walkable;
        this.orientation = orientation;
    }

    public Node(short id, Node parent, OrientationEnum orientation, Point position, boolean walkable) {
        this.id = id;
        this.parent = parent;
        this.orientation = orientation;
        this.position = position;
        this.walkable = walkable;
    }

    public Node(Node parent, OrientationEnum orientation, Cell cell) {
        this.id = cell.getId();
        this.parent = parent;
        this.orientation = orientation;
        this.position = cell.getPosition();
        this.walkable = cell.isWalkable();
    }

    @Override
    public short getId() {
        return id;
    }

    @Override
    public Node getParent() {
        return parent;
    }

    @Override
    public OrientationEnum getOrientation() {
        return orientation;
    }

    @Override
    public Point getPosition() {
        return position;
    }

    @Override
    public boolean isWalkable() {
        return walkable;
    }

    @Override
    public int getF(){
        return g + h;
    }

    @Override
    public int getG() {
        return g;
    }

    @Override
    public void setG(int g) {
        this.g = g;
    }

    @Override
    public int getH() {
        return h;
    }

    @Override
    public void setH(int h) {
        this.h = h;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && (obj == this || obj instanceof INode && equals(obj));
    }

    @Override
    public int hashCode() {
        return id;
    }

    public boolean equals(INode node){
        return node != null && node.getId() == id;
    }
}
