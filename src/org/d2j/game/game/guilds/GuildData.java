package org.d2j.game.game.guilds;

import org.d2j.game.model.Guild;
import org.d2j.game.model.GuildMember;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 04/02/12
 * Time: 11:16
 * To change this template use File | Settings | File Templates.
 */
public class GuildData {
    private Guild guild;
    private GuildMember member;

    public GuildData(Guild guild, GuildMember member) {
        this.guild = guild;
        this.member = member;
    }

    public Guild getGuild() {
        return guild;
    }

    public void setGuild(Guild guild) {
        this.guild = guild;
    }

    public GuildMember getMember() {
        return member;
    }

    public void setMember(GuildMember member) {
        this.member = member;
    }
}
