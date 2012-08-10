package org.d2j.utils.database.repository;

import org.d2j.utils.Action1;
import org.d2j.utils.database.entity.IEntity;

/**
 * User: Blackrush
 * Date: 26/12/11
 * Time: 13:57
 * IDE : IntelliJ IDEA
 */
public interface IEntityRepository<T extends IEntity<TKey>, TKey>
        extends ISaveableEntityRepository<T, TKey>
{
    void create(T entity);
    void delete(T entity);
    boolean delete(TKey id);

    void addListenerOnCreated(Action1<T> listener);
    void addListenerOnDeleted(Action1<T> listener);
}
