package org.d2j.utils.database;

import org.apache.commons.configuration.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Blackrush
 * Date: 27/10/11
 * Time: 19:08
 * IDE : IntelliJ IDEA
 */
public class ConnectionStringBuilder {
    public static ConnectionStringBuilder loadFromConfiguration(Configuration configuration, String prefix){
        return new ConnectionStringBuilder(
                configuration.getString(prefix + "hostname"),
                configuration.getString(prefix + "database"),
                configuration.getString(prefix + "user"),
                configuration.getString(prefix + "password")
        );
    }

    private String hostname;
    private String database;
    private String username;
    private String password;

    public ConnectionStringBuilder(String hostname, String database, String username, String password) {
        this.hostname = hostname;
        this.database = database;
        this.username = username;
        this.password = password;
    }

    @Override
    public String toString() {
        return "jdbc:mysql://" + hostname + ":3306/" + database + "?zeroDateTimeBehavior=convertToNull";
    }

    public String getConnectionString() {
        return toString();
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
