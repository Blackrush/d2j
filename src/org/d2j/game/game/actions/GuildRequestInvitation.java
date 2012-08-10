package org.d2j.game.game.actions;

import org.d2j.common.NetworkStringBuffer;
import org.d2j.common.client.protocol.GuildGameMessageFormatter;
import org.d2j.common.client.protocol.InfoGameMessageFormatter;
import org.d2j.common.client.protocol.enums.GuildInvitationErrorEnum;
import org.d2j.common.client.protocol.enums.GuildMemberRightsEnum;
import org.d2j.game.IRepositoryManager;
import org.d2j.game.configuration.IGameConfiguration;
import org.d2j.game.game.guilds.GuildData;
import org.d2j.game.model.Guild;
import org.d2j.game.model.GuildMember;
import org.d2j.game.service.game.GameClient;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 07/02/12
 * Time: 21:58
 * To change this template use File | Settings | File Templates.
 */
public class GuildRequestInvitation implements IInvitation {
    private final IGameConfiguration configuration;
    private final IRepositoryManager repositories;
    private final GameClient source, target;

    public GuildRequestInvitation(IGameConfiguration configuration, IRepositoryManager repositories, GameClient source, GameClient target) {
        this.configuration = configuration;
        this.repositories = repositories;
        this.source = source;
        this.target = target;
    }

    @Override
    public GameActionType getActionType() {
        return GameActionType.GUILD_REQUEST_INVITATION;
    }

    @Override
    public void accept() throws GameActionException {
        Guild guild = source.getCharacter().getGuildData().getGuild();

        GuildMember member = guild.addMember(target.getCharacter());
        GuildData data = new GuildData(guild, member);
        target.getCharacter().setGuildData(data);
        repositories.getGuildMembers().create(member);

        source.getSession().write(GuildGameMessageFormatter.
                invitationRemoteSuccessMessage(target.getCharacter().getName()));

        try (NetworkStringBuffer buf = new NetworkStringBuffer(target.getSession())){
            buf.append(GuildGameMessageFormatter.statsMessage(
                    guild.getName(),
                    guild.getEmblem(),
                    member.getRights().toInt()
            ));
            buf.append(GuildGameMessageFormatter.invitationLocalSuccessMessage());
        }

        source.getActions().pop();
        target.getActions().pop();
    }

    @Override
    public void decline() throws GameActionException {
        source.getSession().write(GuildGameMessageFormatter.invitationRemoteFailureMessage());

        source.getActions().pop();
        target.getActions().pop();
    }

    @Override
    public void begin() throws GameActionException {
        Guild guild = source.getCharacter().getGuildData().getGuild();

        if (!source.getCharacter().getGuildData().getMember().getRights().get(GuildMemberRightsEnum.INVITE)){
            source.getSession().write(GuildGameMessageFormatter.
                    invitationErrorMessage(GuildInvitationErrorEnum.NOT_ENOUGH_RIGHTS));

            source.getActions().pop();
            target.getActions().pop();
        }
        else if (target.getCharacter().getGuildData() != null){
            source.getSession().write(GuildGameMessageFormatter.
                    invitationErrorMessage(GuildInvitationErrorEnum.ALREADY_IN_GUILD));

            source.getActions().pop();
            target.getActions().pop();
        }
        else if (guild.getMembers().size() >= (configuration.getMaxGuildMembers() + guild.getExperience().getLevel())){
            source.getSession().write(InfoGameMessageFormatter.
                    fullGuildMessage(configuration.getMaxGuildMembers() + guild.getExperience().getLevel()));

            source.getActions().pop();
            target.getActions().pop();
        }
        else{
            source.getSession().write(GuildGameMessageFormatter.invitationLocalMessage(target.getCharacter().getName()));
            target.getSession().write(GuildGameMessageFormatter.invitationRemoteMessage(
                    source.getCharacter().getId(),
                    source.getCharacter().getName(),
                    source.getCharacter().getGuildData().getGuild().getName()
            ));
        }
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
