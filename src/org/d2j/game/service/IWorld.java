package org.d2j.game.service;

import org.d2j.common.client.protocol.enums.ChannelEnum;
import org.d2j.common.client.protocol.enums.WorldStateEnum;
import org.d2j.game.IRepositoryManager;
import org.d2j.game.configuration.IGameConfiguration;
import org.d2j.game.game.events.InfoMessageEvent;
import org.d2j.game.game.events.SystemMessageEvent;
import org.d2j.game.model.Character;
import org.d2j.game.service.game.GameService;
import org.d2j.game.service.login.ILoginServerManager;
import org.joda.time.Duration;

import java.util.Collection;
import java.util.Observer;

/**
 * User: Blackrush
 * Date: 24/12/11
 * Time: 16:40
 * IDE : IntelliJ IDEA
 */
public interface IWorld {
    ILoginServerManager getLoginServerManager();
    IRepositoryManager getRepositoryManager();
    GameService getGameService();
    IGameConfiguration getConfiguration();

    WorldStateEnum getState();
    void setState(WorldStateEnum state);

    int getCompletion();
    void setCompletion(int completion);

    Duration uptime();
    Collection<Character> getOnlineStaff();
    Collection<Character> getOnlinePlayers();

    void speak(Character actor, ChannelEnum channel, String message);

    void addObserver(Observer observer);
    void deleteObserver(Observer observer);

    void systemMessage(SystemMessageEvent event);
    void infoMessage(InfoMessageEvent event);
    void alertMessage(String message);
}
