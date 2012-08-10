package org.d2j.game.game.pathfinding;

/**
 * User: Blackrush
 * Date: 27/11/11
 * Time: 10:01
 * IDE : IntelliJ IDEA
 */
public class SortedNodeList<T extends INode> extends NodeList<T> {
    @Override
    public boolean add(T node) {
        int left = 0, right = size() - 1;
        while (left <= right){
            int center = (left + right) / 2;
            T current = get(center);
            if (node.getF() < current.getF()){
                right = center - 1;
            }
            else if (node.getF() > current.getF()){
                left = center + 1;
            }
            else{
                left = center;
                break;
            }
        }

        add(left, node);

        return true;
    }
}
