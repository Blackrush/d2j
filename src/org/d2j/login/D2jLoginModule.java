package org.d2j.login;

import com.google.inject.AbstractModule;
import org.d2j.login.configuration.ILoginConfiguration;
import org.d2j.login.configuration.MemoryLoginConfiguration;
import org.d2j.login.configuration.PropertiesLoginConfiguration;

/**
 * User: Blackrush
 * Date: 26/12/11
 * Time: 11:23
 * IDE : IntelliJ IDEA
 */
public class D2jLoginModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(ILoginConfiguration.class).to(MemoryLoginConfiguration.class);
    }
}
