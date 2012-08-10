package org.d2j.game.game.fights;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User: Blackrush
 * Date: 23/12/11
 * Time: 11:49
 * IDE : IntelliJ IDEA
 */
public class AppendableFightHandlerAction implements FightHandlerAction {
    private List<FightHandlerAction> actions = new ArrayList<>();

    @SafeVarargs
    public AppendableFightHandlerAction(FightHandlerAction... actions) {
        Collections.addAll(this.actions, actions);
    }

    @Override
    public void call(IFightHandler obj) throws FightException {
        for (FightHandlerAction action : actions){
            action.call(obj);
        }
    }

    public void append(FightHandlerAction action){
        actions.add(action);
    }
}
