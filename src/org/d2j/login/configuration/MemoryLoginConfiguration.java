package org.d2j.login.configuration;

import org.d2j.utils.database.ConnectionStringBuilder;

import javax.inject.Singleton;

/**
 * User: Blackrush
 * Date: 29/10/11
 * Time: 17:18
 * IDE : IntelliJ IDEA
 */
@Singleton
public class MemoryLoginConfiguration implements ILoginConfiguration {
    public String getClientVersion() {
        return "1.29.1";
    }

    public ConnectionStringBuilder getConnectionInformations() {
        return new ConnectionStringBuilder("localhost", "d2j_login", "root", "");
    }

    public int getExecutionInterval() {
        return 5 * 60;
    }

    public int getCommunity() {
        return 0;
    }

    public int getRefreshInterval() {
        return 2 * 60;
    }

    public int getRemotePort() {
        return 443;
    }

    public int getSystemPort() {
        return 4443;
    }
}
