package org.d2j.utils.database.repository;

import org.d2j.utils.database.EntitiesContext;
import org.d2j.utils.database.entity.ISaveableEntity;
import org.d2j.utils.database.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * User: Blackrush
 * Date: 30/10/11
 * Time: 09:18
 * IDE : IntelliJ IDEA
 */
public abstract class AbstractRefreshableEntityRepository<T extends ISaveableEntity<TKey>, TKey>
    extends AbstractSaveableEntityRepository<T, TKey>
    implements ActionListener
{
    protected boolean started;
    private Timer timer;
    private Logger logger;

    protected AbstractRefreshableEntityRepository(EntitiesContext context, int refreshInterval) {
        super(context);

        this.timer = new Timer(refreshInterval * 1000, this);
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    protected AbstractRefreshableEntityRepository(EntitiesContext context, int refreshInterval, Logger logger) {
        super(context);

        this.timer = new Timer(refreshInterval * 1000, this);
        this.logger = logger;
    }

    protected abstract String getRefreshQuery();
    protected abstract String getRefreshedQuery();
    protected abstract TKey readId(ResultSet reader) throws SQLException;
    protected abstract void refresh(T entity, ResultSet reader) throws SQLException;

    public void start(){
        if (started) return;

        timer.start();
    }

    public void stop(){
        if (!started) return;

        timer.stop();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final long[] refreshed = {0};
        context.query(new Query() {
            public void query(Statement statement) throws Exception {
                ResultSet results = statement.executeQuery(getRefreshQuery());
                results.beforeFirst();
                while (results.next()){
                    TKey id = readId(results);
                    T entity = entities.get(id);
                    if (entity != null){
                        refresh(entity, results);
                    } else {
                        entity = loadOne(results);
                        entities.put(id, entity);
                    }
                    ++refreshed[0];
                }
                results.close();
            }
        });

        context.executeAndCommit(getRefreshedQuery());
        logger.info("{} entities refreshed.", refreshed[0]);
    }
}
