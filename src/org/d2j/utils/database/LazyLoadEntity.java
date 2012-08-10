package org.d2j.utils.database;

import org.d2j.utils.AbstractLazyLoad;
import org.d2j.utils.LazyLoad;
import org.d2j.utils.Maker;
import org.d2j.utils.database.entity.IBaseEntity;
import org.d2j.utils.database.repository.IBaseEntityRepository;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 04/02/12
 * Time: 11:49
 * To change this template use File | Settings | File Templates.
 */
public class LazyLoadEntity<T extends IBaseEntity<TKey>, TKey> extends AbstractLazyLoad<T> {
    private IBaseEntityRepository<T, TKey> repository;
    private TKey id;

    public LazyLoadEntity(IBaseEntityRepository<T, TKey> repository) {
        this.repository = repository;
    }

    public LazyLoadEntity(final IBaseEntityRepository<T, TKey> repository, final TKey id) {
        this.repository = repository;
        this.id = id;
    }

    public TKey getId() {
        return id;
    }

    public void setId(TKey id) {
        this.id = id;
    }

    @Override
    protected T refresh() {
        return repository.findById(id);
    }
}
