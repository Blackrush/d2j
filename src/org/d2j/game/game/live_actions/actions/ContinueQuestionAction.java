package org.d2j.game.game.live_actions.actions;

import org.d2j.game.game.actions.GameActionException;
import org.d2j.game.game.actions.NpcDialogAction;
import org.d2j.game.game.live_actions.ILiveAction;
import org.d2j.game.game.live_actions.LiveActionType;
import org.d2j.game.model.NpcQuestion;
import org.d2j.game.service.game.GameClient;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 30/01/12
 * Time: 22:02
 * To change this template use File | Settings | File Templates.
 */
public class ContinueQuestionAction implements ILiveAction {
    private final NpcQuestion question;

    public ContinueQuestionAction(NpcQuestion question) {
        this.question = question;
    }

    @Override
    public LiveActionType getResponseType() {
        return LiveActionType.CONTINUE_QUESTION;
    }

    @Override
    public void apply(GameClient client) throws GameActionException {
        NpcDialogAction action = client.getActions().current();
        action.setQuestion(question);
    }
}
