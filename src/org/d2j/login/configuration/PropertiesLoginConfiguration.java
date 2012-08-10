package org.d2j.login.configuration;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.exception.NestableException;
import org.d2j.utils.database.ConnectionStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 16/02/12
 * Time: 17:44
 * To change this template use File | Settings | File Templates.
 */
@Singleton
public class PropertiesLoginConfiguration extends PropertiesConfiguration implements ILoginConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(PropertiesLoginConfiguration.class);
    private static final String FILE_NAME = System.getProperty("user.dir") + "/login.properties";

    private int remotePort, systemPort;
    private String clientVersion;
    private int community;
    private ConnectionStringBuilder connectionInformations;
    private int executionInterval;
    private int refreshInterval;

    public PropertiesLoginConfiguration() throws ConfigurationException {
        super(FILE_NAME);

        remotePort = getInt("port.remote");
        systemPort = getInt("port.system");
        clientVersion = getString("client.version", "1.29.1");
        community = getInt("client.community", 0);
        connectionInformations = new ConnectionStringBuilder(
                getString("mysql.hostname", "localhost"),
                getString("mysql.database", "d2j_login"),
                getString("mysql.user", "root"),
                getString("mysql.password", "")
        );
        executionInterval = getInt("mysql.execution.interval", 420);
        refreshInterval = getInt("mysql.refresh.interval", 240);

        logger.info("successfully loaded from {}.", FILE_NAME);
    }

    @Override
    public int getRemotePort() {
        return remotePort;
    }

    @Override
    public int getSystemPort() {
        return systemPort;
    }

    @Override
    public String getClientVersion() {
        return clientVersion;
    }

    @Override
    public int getCommunity() {
        return community;
    }

    @Override
    public ConnectionStringBuilder getConnectionInformations() {
        return connectionInformations;
    }

    @Override
    public int getExecutionInterval() {
        return executionInterval;
    }

    @Override
    public int getRefreshInterval() {
        return refreshInterval;
    }
}
