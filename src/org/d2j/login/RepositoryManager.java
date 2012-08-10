package org.d2j.login;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.d2j.login.configuration.ILoginConfiguration;
import org.d2j.login.repository.AccountRepository;
import org.d2j.utils.database.EntitiesContext;
import org.d2j.utils.database.LoadingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.SQLException;

/**
 * User: Blackrush
 * Date: 30/10/11
 * Time: 09:39
 * IDE : IntelliJ IDEA
 */
@Singleton
public class RepositoryManager extends AbstractModule {
    private static final Logger logger = LoggerFactory.getLogger(RepositoryManager.class);

    private final EntitiesContext context;
    private final ILoginConfiguration configuration;
    private boolean started;

    private final AccountRepository accounts;

    @Inject
    public RepositoryManager(ILoginConfiguration configuration) throws ClassNotFoundException {
        this.context = new EntitiesContext(configuration.getConnectionInformations(), configuration.getExecutionInterval());
        this.configuration = configuration;

        Injector injector = Guice.createInjector(this);

        this.accounts = injector.getInstance(AccountRepository.class);
    }

    public void start(){
        if (started) return;

        try {
            context.start();
            accounts.start();

            logger.info("{} accounts loaded.", accounts.loadAll());

            started = true;

            logger.info("successfully started.");
        } catch (LoadingException e) {
            logger.error("Can't load RepositoryManager.", e.getCause());
        } catch (SQLException e) {
            logger.error("Can't begin RepositoryManager.", e.getCause());
        }
    }

    public void stop(){
        if (!started) return;

        try {
            accounts.stop();
            context.stop();

            started = false;

            logger.info("successfully stoped.");
        } catch (SQLException e) {
            logger.error("Can't end RepositoryManager.", e.getCause());
        }
    }

    public AccountRepository getAccounts() {
        return accounts;
    }

    @Override
    protected void configure() {
        bind(EntitiesContext.class).toInstance(context);
        bind(ILoginConfiguration.class).toInstance(configuration);
    }
}
