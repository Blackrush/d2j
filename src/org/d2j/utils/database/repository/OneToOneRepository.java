package org.d2j.utils.database.repository;

import org.d2j.utils.database.EntitiesContext;
import org.d2j.utils.database.entity.IBaseEntity;
import org.d2j.utils.database.LoadingException;
import org.d2j.utils.database.ReturnQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * User: Blackrush
 * Date: 08/12/11
 * Time: 13:20
 * IDE : IntelliJ IDEA
 */
public abstract class OneToOneRepository
    <T1 extends IBaseEntity<?>,
     A extends IBaseEntityRepository<T1, ?>,
     T2 extends IBaseEntity<?>,
     B extends IBaseEntityRepository<T2, ?>>
    implements ISimpleRepository
{
    private final EntitiesContext context;
    private final Logger logger;
    private boolean loaded;
    protected final A repo1;
    protected final B repo2;

    public OneToOneRepository(EntitiesContext context, A repo1, B repo2) {
        this.context = context;
        this.logger = LoggerFactory.getLogger("OneToOneRepository");
        this.repo1 = repo1;
        this.repo2 = repo2;
    }

    public OneToOneRepository(EntitiesContext context, Logger logger, A repo1, B repo2) {
        this.context = context;
        this.logger = logger;
        this.repo1 = repo1;
        this.repo2 = repo2;
    }

    protected abstract String getLoadQuery();

    protected abstract T1 getFirst(ResultSet result) throws SQLException;
    protected abstract T2 getSecond(ResultSet result) throws SQLException;

    protected abstract void load(T1 first, T2 second) throws LoadingException;

    protected void beforeLoad() throws LoadingException {
        if (!repo1.isLoaded()){
            throw new LoadingException("First repository isn't loaded.");
        }

        if (!repo2.isLoaded()){
            throw new LoadingException("Second repository isn't loaded.");
        }
    }

    protected void afterLoad() throws LoadingException {

    }

    @Override
    public long loadAll() throws LoadingException {
        beforeLoad();

        long result = context.query(new ReturnQuery<Long>() {
            @Override
            public Long query(Statement statement) throws Exception {
                long result = 0;

                ResultSet results = statement.executeQuery(getLoadQuery());
                while (results.next()){
                    try {
                        T1 first = getFirst(results);
                        T2 second = getSecond(results);

                        load(first, second);
                        ++result;
                    } catch (LoadingException e) {
                        logger.error("Unhandled LoadingException : " + e.getMessage(), e.getCause());
                    } catch (SQLException e) {
                        logger.error("Unhandled SQLException : " + e.getMessage(), e.getCause());
                    }
                }

                return result;
            }
        });

        afterLoad();

        loaded = true;

        return result;
    }

    @Override
    public boolean isLoaded() {
        return loaded;
    }
}
