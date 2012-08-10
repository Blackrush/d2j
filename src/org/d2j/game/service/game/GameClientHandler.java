package org.d2j.game.service.game;

import java.util.Observer;

/**
 * User: Blackrush
 * Date: 01/11/11
 * Time: 09:50
 * IDE : IntelliJ IDEA
 */
public abstract class GameClientHandler implements Observer {
    protected GameService service;
    protected GameClient client;

    protected GameClientHandler(GameService service, GameClient client) {
        this.service = service;
        this.client = client;
    }

    public GameService getService(){
        return service;
    }

    public abstract void parse(String packet) throws Exception;
    public abstract void onClosed() throws Exception;
}
