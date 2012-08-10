package org.d2j.game.game.actions;

import org.d2j.game.game.actions.IGameAction;
import org.d2j.game.service.game.GameClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 28/01/12
 * Time: 15:12
 * To change this template use File | Settings | File Templates.
 */
public class ActionList extends Stack<IGameAction> {
    public static interface Listener {
        void listen(GameClient client, IGameAction action);
    }

    private final GameClient client;
    private List<Listener> listeners = new ArrayList<>();
    private final Object listeners_sync = new Object();

    public ActionList(GameClient client) {
        this.client = client;
    }

    public void addListener(Listener listener){
        synchronized (listeners_sync){
            listeners.add(listener);
        }
    }

    public void deleteListener(Listener listener){
        synchronized (listeners_sync){
            listeners.remove(listener);
        }
    }

    @Override
    public IGameAction push(IGameAction action) {
        IGameAction result = super.push(action);

        synchronized (listeners_sync){
            for (Listener listener : listeners){
                listener.listen(client, action);
            }
        }

        return result;
    }

    public <T extends IGameAction> T current(){
        return size() > 0 ? (T)peek() : null;
    }

    public boolean currentActionIs(GameActionType type){
        return size() > 0 && peek().getActionType().equals(type);
    }

    public <T extends IGameAction> T getByActionTypeAndPop(GameActionType type){
        if (size() < 0){
             return null;
        }
        else{
            for (int i = 0; i < size(); i++) {
                IGameAction action = get(i);
                if (action.getActionType() == type) {
                    remove(i);
                    return (T) action;
                }
            }
            return null;
        }
    }
}
