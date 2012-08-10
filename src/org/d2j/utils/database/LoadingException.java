package org.d2j.utils.database;

import java.sql.SQLException;

/**
 * User: Blackrush
 * Date: 06/11/11
 * Time: 11:49
 * IDE:  IntelliJ IDEA
 */
public class LoadingException extends SQLException {
    public LoadingException() {
    }

    public LoadingException(String message) {
        super(message);
    }

    public LoadingException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoadingException(Throwable cause) {
        super(cause);
    }
}
