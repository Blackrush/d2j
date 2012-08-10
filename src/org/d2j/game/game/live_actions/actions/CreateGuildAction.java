package org.d2j.game.game.live_actions.actions;

import org.d2j.common.client.protocol.GuildGameMessageFormatter;
import org.d2j.game.game.actions.GameActionException;
import org.d2j.game.game.actions.NpcDialogAction;
import org.d2j.game.game.live_actions.ILiveAction;
import org.d2j.game.game.live_actions.LiveActionType;
import org.d2j.game.service.game.GameClient;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 04/02/12
 * Time: 10:46
 * To change this template use File | Settings | File Templates.
 */
public class CreateGuildAction implements ILiveAction {
    @Override
    public LiveActionType getResponseType() {
        return LiveActionType.CREATE_GUILD;
    }

    @Override
    public void apply(GameClient client) throws GameActionException {
        client.getSession().write(GuildGameMessageFormatter.startCreationMessage());
    }
}
