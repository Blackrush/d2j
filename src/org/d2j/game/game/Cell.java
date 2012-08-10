package org.d2j.game.game;

import org.d2j.common.StringUtils;
import org.d2j.common.client.protocol.enums.OrientationEnum;
import org.d2j.game.game.pathfinding.INode;
import org.d2j.game.game.pathfinding.Node;
import org.d2j.utils.Point;

import java.util.Collection;
import java.util.HashMap;

/**
 * User: Blackrush
 * Date: 14/11/11
 * Time: 20:39
 * IDE : IntelliJ IDEA
 */
public class Cell {
    public static enum MovementType {
        Unwalkable,
        Door,
        Trigger,
        Walkable,
        Paddock,
        Road;

        private static final HashMap<Integer, MovementType> e = new HashMap<>();
        static{
            for (MovementType type : MovementType.values()){
                e.put(type.ordinal(), type);
            }
        }

        public static MovementType valueOf(Integer ordinal){
            return e.get(ordinal);
        }
    }

    public static char encode(OrientationEnum orientation){
        return StringUtils.HASH.charAt(orientation.ordinal());
    }

    public static String encode(short cellId){
        return Character.toString(StringUtils.HASH.charAt(cellId / 64)) +
               Character.toString(StringUtils.HASH.charAt(cellId % 64));
    }

    public static short decode(String cellCode){
        return (short) (StringUtils.HASH.indexOf(cellCode.charAt(0)) * 64 +
                        StringUtils.HASH.indexOf(cellCode.charAt(1)));
    }

    public static OrientationEnum decode(char orientationCode){
        return OrientationEnum.valueOf(StringUtils.HASH.indexOf(orientationCode));
    }

    public static String encode(Collection<? extends INode> nodes){
        StringBuilder sb = new StringBuilder(2 * nodes.size());
        for (INode node : nodes){
            sb.append(encode(node.getOrientation()));
            sb.append(encode(node.getId()));
        }
        return sb.toString();
    }

    private short id;
    private boolean lineOfSight;
    private MovementType movementType;
    private int groundLevel;
    private int groundSlope;
    private Point position;

    public Cell(short id, boolean lineOfSight, MovementType movementType, int groundLevel, int groundSlope, Point position) {
        this.id = id;
        this.lineOfSight = lineOfSight;
        this.movementType = movementType;
        this.groundLevel = groundLevel;
        this.groundSlope = groundSlope;
        this.position = position;
    }

    public short getId() {
        return id;
    }

    public boolean isLineOfSight() {
        return lineOfSight;
    }

    public MovementType getMovementType() {
        return movementType;
    }

    public int getGroundLevel() {
        return groundLevel;
    }

    public int getGroundSlope() {
        return groundSlope;
    }

    public boolean isWalkable(){
        return movementType != MovementType.Unwalkable;
    }

    public Point getPosition() {
        return position;
    }
}
