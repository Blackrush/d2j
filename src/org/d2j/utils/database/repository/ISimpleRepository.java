package org.d2j.utils.database.repository;

import org.d2j.utils.database.LoadingException;

/**
 * User: Blackrush
 * Date: 08/12/11
 * Time: 13:15
 * IDE : IntelliJ IDEA
 */
public interface ISimpleRepository {
    long loadAll() throws LoadingException;
    boolean isLoaded();
}
