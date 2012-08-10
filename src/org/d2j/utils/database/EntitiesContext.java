package org.d2j.utils.database;

import org.d2j.utils.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * User: Blackrush
 * Date: 27/10/11
 * Time: 19:04
 * IDE : IntelliJ IDEA
 */
public class EntitiesContext implements ActionListener {
    private static final Logger logger = LoggerFactory.getLogger(EntitiesContext.class);

    private Connection connection;
    private boolean started;
    private final ConnectionStringBuilder connectionStringBuilder;
    private final Timer timer;
    private final List<String> queries = new ArrayList<>();

    public EntitiesContext(ConnectionStringBuilder csb, int executionInterval) throws ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");

        this.connectionStringBuilder = csb;
        this.timer = executionInterval > 0 ? new Timer(executionInterval * 1000, this) : null;
    }

    public void start() throws SQLException {
        connection = DriverManager.getConnection(
                connectionStringBuilder.getConnectionString(),
                connectionStringBuilder.getUsername(),
                connectionStringBuilder.getPassword()
        );

        if (timer != null){
            timer.start();
        }

        started = true;

        logger.info("connected to {} on the database {}.", connectionStringBuilder.getHostname(), connectionStringBuilder.getDatabase());
    }

    public void stop() throws SQLException {
        started = false;

        if (timer != null){
            timer.stop();
        }
        connection.close();
    }

    public void execute(String query){
        if (query == null || query.isEmpty()) return;

        synchronized (queries){
            queries.add(query);
        }
    }

    public boolean executeAndCommit(String query){
        try (Statement statement = connection.createStatement()) {
            return statement.execute(query);
        } catch (SQLException e) {
            logger.warn("Unhandled SQL exception. {}", e.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public synchronized void query(Query query){
        try (Statement statement = connection.createStatement()) {
            query.query(statement);
        } catch (SQLException e) {
            logger.warn("Unhandled SQL exception. {}", e.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized <T> T query(ReturnQuery<T> query){
        T obj = null;

        try (Statement statement = connection.createStatement()){
            obj = query.query(statement);
        } catch (SQLException e) {
            logger.warn("Unhandled SQL exception. {}", e.toString());
        } catch (Exception e) {
            logger.warn("Unhandled exception. {}", e.getCause().getMessage());
        }

        return obj;
    }

    public void commit(){
        actionPerformed(null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!started) return;

        synchronized (queries){
            long count = 0;
            try (Statement statement = connection.createStatement()){
                while (queries.size() > 0){
                    String query = queries.remove(0);
                    if (query == null) continue;

                    try{
                        statement.execute(query);
                        ++count;
                    } catch (SQLException e2) {
                        logger.warn("Unhandled SQL exception.\nQuery: {}\n{}", query, e2.toString());
                    }
                }
            } catch (SQLException e1) {
                logger.warn("Unhandled SQL exception. {}", e1.getCause().getMessage());
            }

            logger.info("commited. {} queries executed.", count);
        }
    }
}
