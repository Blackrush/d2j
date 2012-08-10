package org.d2j.game.game.actions;

import org.d2j.common.NetworkStringBuffer;
import org.d2j.common.NetworkStringBuilder;
import org.d2j.common.client.protocol.PartyGameMessageFormatter;
import org.d2j.game.game.Party;
import org.d2j.game.model.Character;
import org.d2j.game.service.game.GameClient;

/**
 * User: Blackrush
 * Date: 22/12/11
 * Time: 20:19
 * IDE : IntelliJ IDEA
 */
public class PartyRequestInvitation implements IInvitation {
    private final GameClient source, target;

    public PartyRequestInvitation(GameClient source, GameClient target) {
        this.source = source;
        this.target = target;
    }

    public GameClient getSource() {
        return source;
    }

    public GameClient getTarget() {
        return target;
    }

    @Override
    public GameActionType getActionType() {
        return GameActionType.PARTY_REQUEST_INVITATION;
    }

    @Override
    public void accept() throws GameActionException {
        Party party = source.getParty();

        String packet = NetworkStringBuilder.combine(
                PartyGameMessageFormatter.createPartyMessage(source.getCharacter().getName()),
                PartyGameMessageFormatter.leaderInformationMessage(source.getCharacter().getId())
        );

        if (party == null){
            party = new Party(source.getCharacter());
            source.setParty(party);

            try (NetworkStringBuffer buf = new NetworkStringBuffer(source.getSession())){
                buf.append(packet);
                buf.append(PartyGameMessageFormatter.addMemberMessage(source.getCharacter().toBasePartyMemberType()));
                buf.append(PartyGameMessageFormatter.declineInvitationMessage());
            }

            party.addObserver(source.getHandler());
        }

        target.setParty(party);

        party.addMember(target.getCharacter());

        try (NetworkStringBuffer buf = new NetworkStringBuffer(target.getSession())){
            buf.append(packet);
            buf.append(PartyGameMessageFormatter.addMembersMessage(
                    Character.toBasePartyMemberType(party.getMembers())
            ));
        }

        party.addObserver(target.getHandler());

        source.getActions().pop();
        target.getActions().pop();
    }

    @Override
    public void decline() throws GameActionException {
        String packet = PartyGameMessageFormatter.declineInvitationMessage();
        source.getSession().write(packet);
        target.getSession().write(packet);

        source.getActions().pop();
        target.getActions().pop();
    }

    @Override
    public void begin() throws GameActionException {
        String packet = PartyGameMessageFormatter.invitationSuccessMessage(
                source.getCharacter().getName(),
                target.getCharacter().getName()
        );

        source.getSession().write(packet);
        target.getSession().write(packet);
    }

    @Override
    public void end() throws GameActionException {
        decline();
    }

    @Override
    public void cancel() throws GameActionException {
        decline();
    }
}
