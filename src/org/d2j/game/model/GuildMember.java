package org.d2j.game.model;

import org.d2j.common.client.protocol.type.BaseGuildMemberType;
import org.d2j.game.game.guilds.RightList;
import org.d2j.utils.database.entity.IEntity;
import org.joda.time.Instant;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 04/02/12
 * Time: 11:17
 * To change this template use File | Settings | File Templates.
 */
public class GuildMember implements IEntity<Long> {
    public static Collection<BaseGuildMemberType> toBaseGuildMemberType(Collection<GuildMember> members){
        List<BaseGuildMemberType> result = new ArrayList<>(members.size());
        for (GuildMember member : members){
            result.add(member.toBaseGuildMemberType());
        }
        return result;
    }

    private long id;
    private Guild guild;
    private Character member;
    private int rank;
    private RightList rights;
    private byte experienceRate;
    private long experienceGiven;

    public GuildMember(long id, Guild guild, Character member, int rank, RightList rights, byte experienceRate, long experienceGiven) {
        this.id = id;
        this.guild = guild;
        this.member = member;
        this.rank = rank;
        this.rights = rights;
        this.experienceRate = experienceRate;
        this.experienceGiven = experienceGiven;
    }

    public GuildMember(Guild guild, Character member, int rank) {
        this.guild = guild;
        this.member = member;
        this.rank = rank;
        this.rights = new RightList().fill(rank == Guild.LEADER);
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Guild getGuild() {
        return guild;
    }

    public void setGuild(Guild guild) {
        this.guild = guild;
    }

    public Character getMember() {
        return member;
    }

    public void setMember(Character member) {
        this.member = member;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public boolean isLeader(){
        return rank == Guild.LEADER;
    }

    public RightList getRights() {
        return rights;
    }

    public void setRights(RightList rights) {
        this.rights = rights;
    }

    public byte getExperienceRate() {
        return experienceRate;
    }

    public void setExperienceRate(byte experienceRate) {
        this.experienceRate = experienceRate;
    }

    public long getExperienceGiven() {
        return experienceGiven;
    }

    public void setExperienceGiven(long experienceGiven) {
        this.experienceGiven = experienceGiven;
    }

    public BaseGuildMemberType toBaseGuildMemberType(){
        return new BaseGuildMemberType(
                member.getId(),
                member.getName(),
                member.getExperience().getLevel(),
                member.getSkin(),
                rank,
                experienceRate,
                experienceGiven,
                rights.toInt(),
                member.getOwner().isOnline(),
                0,
                new Instant(member.getOwner().getLastConnection().getTime())
        );
    }

    @Override
    public void beforeSave() {
    }

    @Override
    public void onSaved() {
    }
}
