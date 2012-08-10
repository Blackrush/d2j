package org.d2j.game.game.live_actions;

import org.d2j.game.game.actions.GameActionException;
import org.d2j.game.game.actions.NpcDialogAction;
import org.d2j.game.game.live_actions.ILiveAction;
import org.d2j.game.game.live_actions.ILiveActionFactory;
import org.d2j.game.game.live_actions.LiveActionFactory;
import org.d2j.game.game.live_actions.LiveActionType;
import org.d2j.game.service.game.GameClient;
import org.d2j.utils.AbstractLazyLoad;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 31/01/12
 * Time: 09:33
 * To change this template use File | Settings | File Templates.
 */
public class LazyLoadLiveAction extends AbstractLazyLoad<ILiveAction> implements ILiveAction {
    private LiveActionType type;
    private String[] arguments;

    private final ILiveActionFactory factory;

    public LazyLoadLiveAction(LiveActionType type, String[] arguments) {
        this.type = type;
        this.arguments = arguments;
        this.factory = LiveActionFactory.getInstance();
    }

    public LazyLoadLiveAction(LiveActionType type, String[] arguments, ILiveActionFactory factory) {
        this.type = type;
        this.arguments = arguments;
        this.factory = factory;
    }

    @Override
    public LiveActionType getResponseType() {
        return get().getResponseType();
    }

    @Override
    public void apply(GameClient client) throws GameActionException {
        get().apply(client);
    }

    @Override
    protected ILiveAction refresh() {
        return factory.make(type, arguments);
    }
}
