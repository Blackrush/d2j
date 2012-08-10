package org.d2j.game;

import com.google.inject.AbstractModule;
import org.d2j.game.configuration.IGameConfiguration;
import org.d2j.game.configuration.MemoryGameConfiguration;
import org.d2j.game.configuration.PropertiesGameConfiguration;
import org.d2j.game.service.IWorld;
import org.d2j.game.service.World;
import org.d2j.game.service.login.ILoginServerManager;
import org.d2j.game.service.login.LoginServerManager;

/**
 * User: Blackrush
 * Date: 24/12/11
 * Time: 16:21
 * IDE : IntelliJ IDEA
 */
public class D2jGameModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(IGameConfiguration.class).to(MemoryGameConfiguration.class);
        bind(IWorld.class).to(World.class);
        bind(ILoginServerManager.class).to(LoginServerManager.class);
        bind(IRepositoryManager.class).to(RepositoryManager.class);
    }
}
