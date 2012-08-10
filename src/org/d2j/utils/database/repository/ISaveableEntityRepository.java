package org.d2j.utils.database.repository;

import org.d2j.utils.Action1;
import org.d2j.utils.database.entity.ISaveableEntity;

import java.sql.SQLException;

/**
 * User: Blackrush
 * Date: 26/12/11
 * Time: 13:54
 * IDE : IntelliJ IDEA
 */
public interface ISaveableEntityRepository<T extends ISaveableEntity<TKey>, TKey> extends IBaseEntityRepository<T, TKey> {
    long saveAll() throws SQLException;

    void save(T entity);

    void addListenerOnSaved(Action1<T> listener);
}
