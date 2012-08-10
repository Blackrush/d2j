package org.d2j.utils.database.repository;

import org.d2j.utils.Predicate;
import org.d2j.utils.Selector;
import org.d2j.utils.database.LoadingException;
import org.d2j.utils.database.entity.IBaseEntity;

import java.util.Collection;

/**
 * User: Blackrush
 * Date: 31/10/11
 * Time: 14:42
 * IDE : IntelliJ IDEA
 */
public interface IBaseEntityRepository<T extends IBaseEntity<TKey>, TKey> extends ISimpleRepository, Iterable<T> {
    IBaseEntityRepository<T, TKey> getProxy();

    T loadOne(TKey id) throws LoadingException;

    T findById(TKey id);

    Collection<T> all();
    Collection<T> where(Predicate<T> predicate);
    <E> Collection<E> select(Selector<T, E> selector);
}
