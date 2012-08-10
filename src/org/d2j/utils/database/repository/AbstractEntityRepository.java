package org.d2j.utils.database.repository;

import org.d2j.utils.Action1;
import org.d2j.utils.database.EntitiesContext;
import org.d2j.utils.database.entity.IEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * User: Blackrush
 * Date: 30/10/11
 * Time: 09:13
 * IDE : IntelliJ IDEA
 */
public abstract class AbstractEntityRepository<T extends IEntity<TKey>, TKey>
        extends AbstractSaveableEntityRepository<T, TKey>
        implements IEntityRepository<T, TKey>
{
    private List<Action1<T>> onCreatedListeners = Collections.synchronizedList(new ArrayList<Action1<T>>());
    private List<Action1<T>> onDeletedListeners = Collections.synchronizedList(new ArrayList<Action1<T>>());

    protected AbstractEntityRepository(EntitiesContext context) {
        super(context);
    }

    protected abstract void setNextId(T entity);
    protected abstract String getCreateQuery(T entity);
    protected abstract String getDeleteQuery(T entity);

    @Override
    public synchronized void create(T entity){
        if (entity == null) return;

        setNextId(entity);
        context.execute(getCreateQuery(entity));
        entities.put(entity.getId(), entity);

        for (Action1<T> listener : onCreatedListeners){
            listener.call(entity);
        }
    }

    @Override
    public synchronized void delete(T entity){
        if (entity == null) return;

        context.execute(getDeleteQuery(entity));
        entities.remove(entity.getId());

        for (Action1<T> listener : onDeletedListeners){
            listener.call(entity);
        }
    }

    @Override
    public synchronized boolean delete(TKey id) {
        for (Map.Entry<TKey, T> entry : entities.entrySet()){
            if (entry.getKey().equals(id)){
                delete(entry.getValue());
                return true;
            }
        }
        return false;
    }

    public void addListenerOnCreated(Action1<T> listener){
        onCreatedListeners.add(listener);
    }

    public void addListenerOnDeleted(Action1<T> listener){
        onDeletedListeners.add(listener);
    }
}
