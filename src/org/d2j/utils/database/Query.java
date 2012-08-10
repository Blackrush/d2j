package org.d2j.utils.database;

import java.sql.Statement;

/**
 * User: Blackrush
 * Date: 27/10/11
 * Time: 20:09
 * IDE : IntelliJ IDEA
 */
public interface Query {
    void query(Statement statement) throws Exception;
}

