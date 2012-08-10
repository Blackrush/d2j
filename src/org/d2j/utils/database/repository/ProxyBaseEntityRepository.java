package org.d2j.utils.database.repository;

import org.d2j.utils.Predicate;
import org.d2j.utils.Selector;
import org.d2j.utils.database.LoadingException;
import org.d2j.utils.database.entity.IBaseEntity;

import java.util.Collection;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: Blackrush
 * Date: 06/03/12
 * Time: 13:12
 */
public class ProxyBaseEntityRepository<T extends IBaseEntity<TKey>, TKey>
    implements IBaseEntityRepository<T, TKey>
{
    private IBaseEntityRepository<T, TKey> instance;

    public IBaseEntityRepository<T, TKey> getInstance() {
        return instance;
    }

    public void setInstance(IBaseEntityRepository<T, TKey> instance) {
        this.instance = instance;
    }

    @Override
    public IBaseEntityRepository<T, TKey> getProxy() {
        return this;
    }

    @Override
    public T loadOne(TKey id) throws LoadingException {
        return instance.loadOne(id);
    }

    @Override
    public T findById(TKey id) {
        return instance.findById(id);
    }

    @Override
    public Collection<T> all() {
        return instance.all();
    }

    @Override
    public Collection<T> where(Predicate<T> tPredicate) {
        return instance.where(tPredicate);
    }

    @Override
    public <E> Collection<E> select(Selector<T, E> teSelector) {
        return instance.select(teSelector);
    }

    @Override
    public long loadAll() throws LoadingException {
        return instance.loadAll();
    }

    @Override
    public boolean isLoaded() {
        return instance.isLoaded();
    }

    @Override
    public Iterator<T> iterator() {
        return instance.iterator();
    }
}
