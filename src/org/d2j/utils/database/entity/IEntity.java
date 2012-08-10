package org.d2j.utils.database.entity;

/**
 * User: Blackrush
 * Date: 30/10/11
 * Time: 00:41
 * IDE : IntelliJ IDEA
 */
public interface IEntity<TKey> extends ISaveableEntity<TKey> {
    void setId(TKey id);
}
