package org.d2j.login.configuration;

import org.d2j.utils.database.ConnectionStringBuilder;

/**
 * User: Blackrush
 * Date: 29/10/11
 * Time: 17:06
 * IDE : IntelliJ IDEA
 */
public interface ILoginConfiguration {
    int getRemotePort();
    int getSystemPort();
    String getClientVersion();
    int getCommunity();

    ConnectionStringBuilder getConnectionInformations();
    int getExecutionInterval();
    int getRefreshInterval();
}
