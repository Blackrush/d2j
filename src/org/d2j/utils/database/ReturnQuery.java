package org.d2j.utils.database;

import java.sql.Statement;

public interface ReturnQuery<T> {
    T query(Statement statement) throws Exception;
}
