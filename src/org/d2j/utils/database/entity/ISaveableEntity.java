package org.d2j.utils.database.entity;

/**
 * User: Blackrush
 * Date: 30/10/11
 * Time: 00:40
 * IDE : IntelliJ IDEA
 */
public interface ISaveableEntity<TKey> extends IBaseEntity<TKey> {
    void beforeSave();
    void onSaved();
}
