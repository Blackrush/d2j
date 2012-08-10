package org.d2j.game.game.pathfinding;

import java.util.ArrayList;

/**
 * User: Blackrush
 * Date: 26/11/11
 * Time: 20:18
 * IDE : IntelliJ IDEA
 */
public class NodeList<T extends INode> extends ArrayList<T> {
    public boolean contains(T node){
        return contains(node.getId());
    }

    public boolean contains(short id){
        for (INode node : this) {
            if (node.getId() == id) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return o != null && o instanceof INode && contains(((INode) o).getId());
    }

    public T removeFirst(){
        T first = get(0);
        remove(0);
        return first;
    }
}
