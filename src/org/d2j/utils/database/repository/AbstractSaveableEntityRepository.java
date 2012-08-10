package org.d2j.utils.database.repository;

import org.d2j.utils.Action1;
import org.d2j.utils.database.EntitiesContext;
import org.d2j.utils.database.entity.ISaveableEntity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Blackrush
 * Date: 30/10/11
 * Time: 09:08
 * IDE : IntelliJ IDEA
 */
public abstract class AbstractSaveableEntityRepository<T extends ISaveableEntity<TKey>, TKey>
        extends AbstractBaseEntityRepository<T, TKey>
        implements ISaveableEntityRepository<T, TKey>
{
    private List<Action1<T>> onSavedListeners = new ArrayList<>();

    protected AbstractSaveableEntityRepository(EntitiesContext context) {
        super(context);
    }

    protected abstract String getSaveQuery(T entity);

    public void save(T entity){
        entity.beforeSave();
        context.execute(getSaveQuery(entity));
        entity.onSaved();

        for (Action1<T> listener : onSavedListeners){
            listener.call(entity);
        }
    }

    public long saveAll() throws SQLException {
        for (T entity : entities.values()){
            save(entity);
        }
        return entities.size();
    }

    public void addListenerOnSaved(Action1<T> listener){
        onSavedListeners.add(listener);
    }
}
