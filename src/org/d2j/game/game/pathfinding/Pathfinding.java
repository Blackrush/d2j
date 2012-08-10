package org.d2j.game.game.pathfinding;

import org.d2j.common.client.protocol.enums.OrientationEnum;
import org.d2j.common.client.protocol.enums.SpellEffectsEnum;
import org.d2j.game.game.Cell;
import org.d2j.game.game.fights.Fight;
import org.d2j.game.game.fights.FightCell;
import org.d2j.game.game.fights.IFighter;
import org.d2j.game.game.fights.actions.IFightAction;
import org.d2j.game.game.spells.cell_effects.CellEffect;
import org.d2j.utils.Future;
import org.d2j.utils.Point;
import org.d2j.utils.Vector;

import java.util.*;
import java.util.Map.Entry;

/**
 * User: Blackrush
 * Date: 26/11/11
 * Time: 20:25
 * IDE : IntelliJ IDEA
 */
public class Pathfinding {
    private static HashMap<OrientationEnum, Vector> orientations = new HashMap<>(8);
    static {
        orientations.put(OrientationEnum.NORTH, new Vector(-1, -1));
        orientations.put(OrientationEnum.SOUTH, new Vector(1, 1));

        orientations.put(OrientationEnum.SOUTH_WEST, new Vector(0, 1));
        orientations.put(OrientationEnum.NORTH_WEST, new Vector(-1, 0));

        orientations.put(OrientationEnum.SOUTH_EAST, new Vector(1, 0));
        orientations.put(OrientationEnum.NORTH_EAST, new Vector(0, -1));

        orientations.put(OrientationEnum.EAST, new Vector(1, -1));
        orientations.put(OrientationEnum.WEST, new Vector(-1, 1));
    }

    public static Point position(short cellId, int mapWidth) {
        int _loc5 = (int) Math.floor(cellId / (mapWidth * 2 - 1));
        int _loc6 = cellId - _loc5 * (mapWidth * 2 - 1);
        int _loc7 = _loc6 % mapWidth;
        int x = (cellId - (mapWidth - 1) * (_loc5 - _loc7)) / mapWidth;
        int y = _loc5 - _loc7;

        return new Point(x, y);
    }

    public static int distanceBetween(Point p1, Point p2){
        return Math.abs(p1.getX() - p2.getX()) +
               Math.abs(p1.getY() - p2.getY());
    }

    public static int estimateTime(List<? extends INode> nodes, int mapWidth){
        int time = 0, steps = nodes.size();

        for (int i = 0; i < steps - 1; ++i){
            switch (nodes.get(i + 1).getOrientation()){
                case EAST:
                case WEST:
                    time += ( Math.abs(nodes.get(i).getId() - nodes.get(i + 1).getId()) ) * (int)( steps >= 4 ? 875d / 2.5d : 875d );
                    break;

                case NORTH:
                case SOUTH:
                    time += ( Math.abs(nodes.get(i).getId() - nodes.get(i + 1).getId()) / ( mapWidth * 2 - 1 ) ) * (int)( steps >= 4 ? 875d / 2.5d : 875d );
                    break;

                case NORTH_EAST:
                case SOUTH_EAST:
                    time += ( Math.abs(nodes.get(i).getId() - nodes.get(i + 1).getId()) / ( mapWidth - 1 ) ) * (int)( steps >= 4 ? 625d / 2.5d : 625d );
                    break;

                case NORTH_WEST:
                case SOUTH_WEST:
                    time += ( Math.abs(nodes.get(i).getId() - nodes.get(i + 1).getId()) / (mapWidth - 1) ) * (int)( steps >= 4 ? 625d / 2.5d : 625d );
                    break;
            }
        }

        return time;
    }

    private static void computeG(Point start, INode node){
        node.setG(distanceBetween(start, node.getPosition()));
    }

    private static void computeH(Point end, INode node){
        node.setH(distanceBetween(node.getPosition(), end));
    }

    public static <T extends Cell> T getCellByOrientation(int mapWidth, int mapHeight, T[] cells, T cell, OrientationEnum orientation) {
        switch (orientation) {
            case EAST:
                return cell.getPosition().getX() == mapWidth * 2 - 2 || cell.getPosition().getX() == mapWidth - 1 ? null : cells[cell.getId() + 1];
            case SOUTH_EAST:
                return cell.getPosition().getX() == mapWidth * 2 - 2 || cell.getPosition().getX() == mapWidth - 1 && cell.getPosition().getY() == mapHeight - 1 ? null : cells[cell.getId() + mapWidth];
            case SOUTH:
                return cell.getPosition().getY() == mapHeight - 1 ? null : cells[cell.getId() + ( mapWidth * 2 - 1 )];
            case SOUTH_WEST:
                return cell.getPosition().getX() == 0 && cell.getPosition().getY() == mapHeight - 1 ? null : cells[cell.getId() + ( mapWidth - 1 )];
            case WEST:
                return cell.getPosition().getX() == 0 || cell.getPosition().getX() == mapWidth ? null : cells[cell.getId() - 1];
            case NORTH_WEST:
                return cell.getPosition().getX() == 0 && cell.getPosition().getY() == 0 ? null : cells[cell.getId() - mapWidth];
            case NORTH:
                return cell.getPosition().getY() == 0 ? null : cells[cell.getId() - ( mapWidth * 2 - 1 )];
            case NORTH_EAST:
                return cell.getPosition().getX() == mapWidth * 2 - 2 || cell.getPosition().getX() == mapWidth - 1 && cell.getPosition().getY() == 0 ? null : cells[cell.getId() - mapWidth + 1];
            default:
                throw new IllegalArgumentException("Unknown direction");
        }
    }

    /**
     * Thanks to bouh2
     * @param mapWidth Map's width
     * @param mapHeight Map's height
     * @param cells Map's cells
     * @param node current node
     * @param direction direction
     * @return cell
     */
    public static <N extends INode, C extends Cell> C getCellByOrientation(int mapWidth, int mapHeight, C[] cells, N node, OrientationEnum direction){
        switch (direction) {
            case EAST:
                return node.getPosition().getX() == mapWidth * 2 - 2 || node.getPosition().getX() == mapWidth - 1 ? null : cells[node.getId() + 1];
            case SOUTH_EAST:
                return node.getPosition().getX() == mapWidth * 2 - 2 || node.getPosition().getX() == mapWidth - 1 && node.getPosition().getY() == mapHeight - 1 ? null : cells[node.getId() + mapWidth];
            case SOUTH:
                return node.getPosition().getY() == mapHeight - 1 ? null : cells[node.getId() + ( mapWidth * 2 - 1 )];
            case SOUTH_WEST:
                return node.getPosition().getX() == 0 && node.getPosition().getY() == mapHeight - 1 ? null : cells[node.getId() + ( mapWidth - 1 )];
            case WEST:
                return node.getPosition().getX() == 0 || node.getPosition().getX() == mapWidth ? null : cells[node.getId() - 1];
            case NORTH_WEST:
                return node.getPosition().getX() == 0 && node.getPosition().getY() == 0 ? null : cells[node.getId() - mapWidth];
            case NORTH:
                return node.getPosition().getY() == 0 ? null : cells[node.getId() - ( mapWidth * 2 - 1 )];
            case NORTH_EAST:
                return node.getPosition().getX() == mapWidth * 2 - 2 || node.getPosition().getX() == mapWidth - 1 && node.getPosition().getY() == 0 ? null : cells[node.getId() - mapWidth + 1];
            default:
                throw new IllegalArgumentException("Unknown direction");
        }
    }

    public static OrientationEnum getOrientationFromPoints(Point p1, Point p2){
        Vector vector = Vector.fromPoints(p1, p2);

        for (Entry<OrientationEnum, Vector> entry : orientations.entrySet()){
            if (entry.getValue().sameDirection(vector)){
                return entry.getKey();
            }
        }
        return null;
    }

    private static Collection<Node> adjacentNodes(int mapWidth, int mapHeight, Cell[] cells, Node node){
        return Arrays.asList(
                new Node(node, OrientationEnum.NORTH_EAST, getCellByOrientation(mapWidth, mapHeight, cells, node, OrientationEnum.NORTH_EAST)),
                new Node(node, OrientationEnum.NORTH_WEST, getCellByOrientation(mapWidth, mapHeight, cells, node, OrientationEnum.NORTH_WEST)),
                new Node(node, OrientationEnum.SOUTH_WEST, getCellByOrientation(mapWidth, mapHeight, cells, node, OrientationEnum.SOUTH_WEST)),
                new Node(node, OrientationEnum.SOUTH_EAST, getCellByOrientation(mapWidth, mapHeight, cells, node, OrientationEnum.SOUTH_EAST))
        );
    }

    private static Collection<FightNode> adjacentFightNodes(Fight fight, FightNode node) {
        int mw = fight.getMap().getWidth(), mh = fight.getMap().getHeight();
        FightCell[] mc = fight.getCells();
        return Arrays.asList(
                new FightNode(getCellByOrientation(mw, mh, mc, node, OrientationEnum.NORTH_EAST), node, OrientationEnum.NORTH_EAST),
                new FightNode(getCellByOrientation(mw, mh, mc, node, OrientationEnum.SOUTH_EAST), node, OrientationEnum.SOUTH_EAST),
                new FightNode(getCellByOrientation(mw, mh, mc, node, OrientationEnum.SOUTH_WEST), node, OrientationEnum.SOUTH_WEST),
                new FightNode(getCellByOrientation(mw, mh, mc, node, OrientationEnum.NORTH_WEST), node, OrientationEnum.NORTH_WEST)
        );
    }

    private static List<Node> getPath(List<Node> close){
        List<Node> nodes = new ArrayList<>(close.size());
        Node last = close.get(close.size() - 1);
        while (last.getParent() != null){
            nodes.add(last);
            last = last.getParent();
        }
        Collections.reverse(nodes);
        return nodes;
    }

    private static List<FightNode> getFightPath(List<FightNode> close){
        List<FightNode> nodes = new ArrayList<>(close.size());
        FightNode last = close.get(close.size() - 1);
        while (last.getParent() != null){
            nodes.add(last);
            last = last.getParent();
        }
        Collections.reverse(nodes);
        return nodes;
    }

    public static List<? extends INode> bestPath(int mapWidth, int mapHeight, OrientationEnum pCurrentOrientation, Cell[] pCells, Cell pStart, Cell pEnd) throws PathfindingException {
        if (!pEnd.isWalkable()){
            throw new NotWalkableException("End is not walkable");
        }

        Node start = new Node(pStart.getId(), pStart.getPosition(), true, pCurrentOrientation);

        NodeList<Node> open  = new SortedNodeList<>(), close = new NodeList<>();
        open.add(start);

        while (!close.contains(pEnd.getId()) && !open.isEmpty()){
            Node best = open.removeFirst();
            close.add(best);

            if (best.getId() == pEnd.getId()) break;

            for (Node node : adjacentNodes(mapWidth, mapHeight, pCells, best)){
                if (node.isWalkable() && !close.contains(node) && !open.contains(node)){
                    computeG(start.getPosition(), node);
                    computeH(pEnd.getPosition(),  node);

                    open.add(node);
                }
            }

            if (open.isEmpty()){
                throw new PathfindingException("no solutions");
            }
        }

        return getPath(close);
    }

    public static List<FightNode> bestFightPath(Fight fight, IFighter fighter, FightCell target) throws PathfindingException {
        if (!target.isWalkable()){
            throw new NotWalkableException("target is not walkable");
        }

        FightNode start = new FightNode(fighter.getCurrentCell(), fighter.getCurrentOrientation());

        NodeList<FightNode> open = new SortedNodeList<>(), close = new NodeList<>();
        open.add(start);

        while (!close.contains(target.getId()) && !open.isEmpty()) {
            final FightNode best = open.removeFirst();

            if (best == start){
                close.add(best);
            }
            else{
                if (best.getCell().getCurrentFighter() != null){
                    if (best.getCell().getCurrentFighter().getBuffs().contains(SpellEffectsEnum.Invisible)){
                        break;
                    }
                    continue;
                }

                close.add(best);

                if (best.getCell().getEffects().nbTraps() > 0){
                    break;
                }
            }

            if (best.getId() == target.getId()) break;

            for (FightNode node : adjacentFightNodes(fight, best)) {
                if (node.getCell().isWalkable() && !close.contains(node) && !open.contains(node)){
                    computeG(start.getPosition(), node);
                    computeH(target.getPosition(), node);

                    open.add(node);
                }
            }

            // todo: throw exception if open list is empty ??
        }

        return getFightPath(close);
    }
}
