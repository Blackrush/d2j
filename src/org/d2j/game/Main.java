package org.d2j.game;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.d2j.common.service.protocol.MessageFactory;
import org.d2j.game.configuration.IGameConfiguration;
import org.d2j.game.game.live_actions.LiveActionFactory;
import org.d2j.game.game.spells.EffectFactory;
import org.d2j.game.service.game.GameService;
import org.d2j.game.service.login.ILoginServerManager;
import org.d2j.game.service.login.LoginServerManager;
import org.d2j.utils.Future0;

import java.io.IOException;

/**
 * User: Blackrush
 * Date: 31/10/11
 * Time: 13:18
 * IDE : IntelliJ IDEA
 */
public class Main {
    public static void main(String[] args) throws ClassNotFoundException, IOException, InterruptedException {
        final Injector injector = Guice.createInjector(new D2jGameModule());

        MessageFactory.getInstance().init();

        final IGameConfiguration configuration = injector.getInstance(IGameConfiguration.class);

        final RepositoryManager rm = injector.getInstance(RepositoryManager.class);
        final GameService gs = injector.getInstance(GameService.class);
        final ILoginServerManager lsm = injector.getInstance(LoginServerManager.class);

        EffectFactory.getInstance().load(rm.getSpellTemplates().getProxy());
        rm.start();
        LiveActionFactory.load(configuration, rm);

        gs.start();
        lsm.start();

        waitForInput();

        gs.stop();
        lsm.stop();
        rm.stop();
    }

    private static void waitForInput() throws IOException {
        System.in.read();
    }
}
