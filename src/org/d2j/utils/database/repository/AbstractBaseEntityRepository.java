package org.d2j.utils.database.repository;

import org.d2j.utils.Predicate;
import org.d2j.utils.Selector;
import org.d2j.utils.database.*;
import org.d2j.utils.database.entity.IBaseEntity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: Blackrush
 * Date: 30/10/11
 * Time: 00:42
 * IDE : IntelliJ IDEA
 */
public abstract class AbstractBaseEntityRepository<T extends IBaseEntity<TKey>, TKey>
    implements IBaseEntityRepository<T, TKey>
{
    private boolean loaded;
    private ProxyBaseEntityRepository<T, TKey> proxy = new ProxyBaseEntityRepository<>();

    protected final EntitiesContext context;
    protected Map<TKey, T> entities = new ConcurrentHashMap<>();

    protected AbstractBaseEntityRepository(EntitiesContext context){
        this.context = context;
    }

    protected abstract String getLoadQuery();
    protected abstract String getLoadOneQuery(TKey id);

    protected abstract T loadOne(ResultSet reader) throws SQLException;

    protected void beforeLoading() throws LoadingException {
    }

    protected void afterLoading(){

    }

    public long loadAll() throws LoadingException {
        beforeLoading();

        context.query(new Query() {
            public void query(Statement statement) throws Exception {
                ResultSet results = statement.executeQuery(getLoadQuery());
                while (results.next()){
                    T entity = loadOne(results);
                    if (entity != null)
                        entities.put(entity.getId(), entity);
                }
                results.close();
            }
        });

        loaded = true;
        proxy.setInstance(this);

        afterLoading();

        return entities.size();
    }

    public synchronized T loadOne(final TKey id) throws LoadingException {
        beforeLoading();

        T entity = context.query(new ReturnQuery<T>() {
            public T query(Statement statement) throws Exception {
                ResultSet result = statement.executeQuery(getLoadOneQuery(id));
                result.beforeFirst();
                if (!result.next())
                    return null;
                return loadOne(result);
            }
        });

        if (entity != null){
            entities.put(entity.getId(), entity);
        }

        loaded = true;

        afterLoading();

        return entity;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public IBaseEntityRepository<T, TKey> getProxy() {
        return proxy;
    }

    public synchronized T findById(TKey id){
        return entities.get(id);
    }

    public synchronized boolean contains(TKey id){
        return entities.containsKey(id);
    }

    public Collection<T> all(){
        return entities.values();
    }

    @Override
    public Iterator<T> iterator() {
        return entities.values().iterator();
    }

    @Override
    public Collection<T> where(Predicate<T> predicate) {
        List<T> matched = new ArrayList<>();
        for (T obj : entities.values()){
            if (predicate.test(obj)){
                matched.add(obj);
            }
        }
        return matched;
    }

    @Override
    public <E> Collection<E> select(Selector<T, E> selector) {
        List<E> result = new ArrayList<>(entities.size());
        for (T obj : entities.values()){
            result.add(selector.select(obj));
        }
        return result;
    }
}
