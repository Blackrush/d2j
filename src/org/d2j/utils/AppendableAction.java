package org.d2j.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User: Blackrush
 * Date: 17/12/11
 * Time: 10:56
 * IDE : IntelliJ IDEA
 */
public class AppendableAction<T> implements Action1<T> {
    private List<Action1<T>> actions = new ArrayList<>();

    @SafeVarargs
    public AppendableAction(Action1<T>... actions) {
        Collections.addAll(this.actions, actions);
    }

    @Override
    public void call(T obj) {
        for (Action1<T> action : actions){
            action.call(obj);
        }
    }

    public void append(Action1<T> action){
        actions.add(action);
    }
}
