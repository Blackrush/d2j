package org.d2j.game.game.live_actions;

import org.d2j.game.IRepositoryManager;
import org.d2j.game.configuration.IGameConfiguration;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 30/01/12
 * Time: 21:55
 * To change this template use File | Settings | File Templates.
 */
public interface ILiveActionFactory {
    void load(IGameConfiguration configuration, IRepositoryManager repositoryManager);

    ILiveAction make(LiveActionType type, String[] args);
}
