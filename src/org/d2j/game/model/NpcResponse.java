package org.d2j.game.model;

import org.d2j.game.game.live_actions.ILiveAction;
import org.d2j.utils.LazyLoad;
import org.d2j.utils.Reference;
import org.d2j.utils.database.entity.IBaseEntity;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 30/01/12
 * Time: 19:49
 * To change this template use File | Settings | File Templates.
 */
public class NpcResponse implements IBaseEntity<Integer> {
    private int id;
    private ILiveAction action;
    private Reference<NpcResponse> next;

    public NpcResponse(int id, ILiveAction action, Reference<NpcResponse> next) {
        this.id = id;
        this.action = action;
        this.next = next;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public ILiveAction getAction() {
        return action;
    }

    public NpcResponse getNext() {
        return next != null ? next.get() : null;
    }
}
