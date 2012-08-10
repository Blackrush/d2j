package org.d2j.game.service;

import org.d2j.common.CollectionUtils;
import org.d2j.common.client.protocol.enums.ChannelEnum;
import org.d2j.common.client.protocol.enums.InfoTypeEnum;
import org.d2j.common.client.protocol.enums.WorldStateEnum;
import org.d2j.game.IRepositoryManager;
import org.d2j.game.configuration.IGameConfiguration;
import org.d2j.game.game.events.AlertMessageEvent;
import org.d2j.game.game.events.InfoMessageEvent;
import org.d2j.game.game.events.MessageEvent;
import org.d2j.game.game.events.SystemMessageEvent;
import org.d2j.game.model.Character;
import org.d2j.game.service.game.GameClient;
import org.d2j.game.service.game.GameService;
import org.d2j.game.service.login.ILoginServerManager;
import org.d2j.utils.Predicate;
import org.d2j.utils.Selector;
import org.joda.time.Duration;
import org.joda.time.Instant;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;
import java.util.Date;
import java.util.Observable;

/**
 * User: Blackrush
 * Date: 31/10/11
 * Time: 18:28
 * IDE : IntelliJ IDEA
 */
@Singleton
public class World extends Observable implements IWorld {
    private IGameConfiguration configuration;
    private ILoginServerManager loginServerManager;
    private IRepositoryManager repositoryManager;
    private GameService gameService;
    private WorldStateEnum state;
    private int completion;
    private Instant creation;

    @Inject
    public World(IGameConfiguration configuration, ILoginServerManager loginServerManager, IRepositoryManager repositoryManager, GameService gameService) {
        this.configuration = configuration;
        this.loginServerManager = loginServerManager;
        this.repositoryManager = repositoryManager;
        this.gameService = gameService;
        this.state = WorldStateEnum.ONLINE;
        this.completion = 0;
        this.creation = new Instant();
    }

    public ILoginServerManager getLoginServerManager() {
        return loginServerManager;
    }

    public IRepositoryManager getRepositoryManager() {
        return repositoryManager;
    }

    public GameService getGameService() {
        return gameService;
    }

    @Override
    public IGameConfiguration getConfiguration() {
        return configuration;
    }

    public WorldStateEnum getState() {
        return state;
    }

    public void setState(WorldStateEnum state) {
        if (this.state == WorldStateEnum.ONLINE && state == WorldStateEnum.SAVING){
            infoMessage(new InfoMessageEvent(InfoTypeEnum.START_SAVE));
        }
        else if (this.state == WorldStateEnum.SAVING && state == WorldStateEnum.ONLINE){
            infoMessage(new InfoMessageEvent(InfoTypeEnum.END_SAVE));
        }

        this.state = state;
        if (loginServerManager != null && loginServerManager.isSynchronized()){
            loginServerManager.refreshWorld();
        }
    }

    public int getCompletion() {
        return completion;
    }

    public void setCompletion(int completion) {
        this.completion = completion;
        if (loginServerManager != null && loginServerManager.isSynchronized()){
            loginServerManager.refreshWorld();
        }
    }

    @Override
    public Duration uptime(){
        return new Duration(creation, new Instant());
    }

    public void speak(Character actor, ChannelEnum channel, String message){
        setChanged();
        notifyObservers(new MessageEvent(actor.getId(), actor.getName(), channel, message));
    }

    @Override
    public void systemMessage(SystemMessageEvent event) {
        setChanged();
        notifyObservers(event);
    }

    @Override
    public void infoMessage(InfoMessageEvent event){
        setChanged();
        notifyObservers(event);
    }

    @Override
    public void alertMessage(String message) {
        setChanged();
        notifyObservers(new AlertMessageEvent(message));
    }

    @Override
    public Collection<Character> getOnlineStaff(){
        return CollectionUtils.select(gameService.getOnlinePlayers(), new Selector<GameClient, Character>() {
            @Override
            public Character select(GameClient o) {
                return o.getAccount().hasRights() ? o.getCharacter() : null;
            }
        });
    }

    @Override
    public Collection<Character> getOnlinePlayers(){
        return CollectionUtils.select(gameService.getOnlinePlayers(), new Selector<GameClient, Character>() {
            @Override
            public Character select(GameClient o) {
                return o.getCharacter();
            }
        });
    }
}
