package org.d2j.game.game.actions;

import org.d2j.common.client.protocol.GameMessageFormatter;
import org.d2j.game.game.fights.ChallengeFight;
import org.d2j.game.game.fights.Fight;
import org.d2j.game.game.fights.FightTeamEnum;
import org.d2j.game.service.game.GameClient;
import org.d2j.game.service.game.handler.FightHandler;
import org.d2j.game.service.game.handler.RolePlayHandler;

/**
 * User: Blackrush
 * Date: 14/11/11
 * Time: 19:28
 * IDE : IntelliJ IDEA
 */
public class ChallengeRequestInvitation implements IInvitation {
    private final GameClient sender, target;

    public ChallengeRequestInvitation(GameClient sender, GameClient target) {
        this.sender = sender;
        this.target = target;
    }

    @Override
    public GameActionType getActionType() {
        return GameActionType.CHALLENGE_REQUEST_INVITATION;
    }

    @Override
    public void begin() throws GameActionException {
        String message = GameMessageFormatter.challengeRequestMessage(
                sender.getCharacter().getId(),
                target.getCharacter().getId()
        );

        sender.getSession().write(message);
        target.getSession().write(message);
    }

    @Override
    public void end() throws GameActionException {
        decline();
    }

    @Override
    public void cancel() throws GameActionException {
        decline();
    }

    @Override
    public void accept() throws GameActionException {
        String message = GameMessageFormatter.challengeAcceptedMessage(
                sender.getCharacter().getId(),
                target.getCharacter().getId()
        );

        sender.getSession().write(message);
        target.getSession().write(message);

        RolePlayHandler senderHandler = (RolePlayHandler)sender.getHandler();
        RolePlayHandler targetHandler = (RolePlayHandler)target.getHandler();

        sender.getCharacter().getCurrentMap().deleteObserver(senderHandler);
        target.getCharacter().getCurrentMap().deleteObserver(targetHandler);

        sender.getCharacter().getCurrentMap().removeActor(sender.getCharacter());
        target.getCharacter().getCurrentMap().removeActor(target.getCharacter());

        Fight fight = new ChallengeFight(
                sender.getService().getConfiguration(),
                sender.getCharacter().getCurrentMap()
        );

        sender.setHandler(new FightHandler(
                senderHandler.getService(),
                sender,
                fight,
                FightTeamEnum.CHALLENGER,
                senderHandler
        ));

        target.setHandler(new FightHandler(
                targetHandler.getService(),
                target,
                fight,
                FightTeamEnum.DEFENDER,
                targetHandler
        ));

        fight.init();
    }

    @Override
    public void decline() throws GameActionException {
        String message = GameMessageFormatter.challengeDeclinedMessage(
                sender.getCharacter().getId(),
                target.getCharacter().getId()
        );

        sender.getSession().write(message);
        target.getSession().write(message);
    }

    public GameClient getSender() {
        return sender;
    }

    public GameClient getTarget() {
        return target;
    }
}
