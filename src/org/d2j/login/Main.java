package org.d2j.login;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.d2j.common.service.protocol.MessageFactory;
import org.d2j.login.service.game.GameServerManager;
import org.d2j.login.service.login.LoginService;

import java.io.IOException;

/**
 * User: Blackrush
 * Date: 29/10/11
 * Time: 16:49
 * IDE : IntelliJ IDEA
 */

public class Main {
    public static void main(String[] args) throws Exception {
        MessageFactory.getInstance().init();

        Injector injector = Guice.createInjector(new D2jLoginModule());

        RepositoryManager rm = injector.getInstance(RepositoryManager.class);
        GameServerManager gsm = injector.getInstance(GameServerManager.class);
        LoginService ls = injector.getInstance(LoginService.class);

        rm.start();
        gsm.start();
        ls.start();

        waitForInput();

        ls.stop();
        gsm.stop();
        rm.stop();
    }

    private static void waitForInput(){
        try {
            System.in.read();
        } catch (IOException e) { }
    }
}
