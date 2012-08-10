package org.d2j.game.repository;

import org.d2j.common.StringUtils;
import org.d2j.game.game.guilds.GuildData;
import org.d2j.game.game.guilds.RightList;
import org.d2j.game.model.*;
import org.d2j.game.model.Character;
import org.d2j.utils.database.EntitiesContext;
import org.d2j.utils.database.LoadingException;
import org.d2j.utils.database.annotation.Dynamic;
import org.d2j.utils.database.repository.AbstractEntityRepository;
import org.d2j.utils.database.repository.IEntityRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 04/02/12
 * Time: 11:26
 * To change this template use File | Settings | File Templates.
 */
@Singleton
public class GuildMemberRepository extends AbstractEntityRepository<GuildMember, Long> {
    private final IEntityRepository<Guild, Integer> guilds;
    private final IEntityRepository<Character, Long> characters;

    private long nextId;

    @Inject
    protected GuildMemberRepository(@Dynamic EntitiesContext context, IEntityRepository<Guild, Integer> guilds, IEntityRepository<Character, Long> characters) {
        super(context);
        this.guilds = guilds;
        this.characters = characters;
    }

    @Override
    protected void setNextId(GuildMember entity) {
        entity.setId(++nextId);
    }

    @Override
    protected String getCreateQuery(GuildMember entity) {
        return StringUtils.format("INSERT INTO `guild_members`(`id`,`guild`,`member`,`rank`,`rights`,`experienceRate`,`experienceGiven`) VALUES('{0}','{1}','{2}','{3}','{4}','{5}','{6}');",
                entity.getId(),
                entity.getGuild().getId(),
                entity.getMember().getId(),
                entity.getRank(),
                entity.getRights().toInt(),
                entity.getExperienceRate(),
                entity.getExperienceGiven()
        );
    }

    @Override
    protected String getDeleteQuery(GuildMember entity) {
        return "DELETE FROM `guild_members` WHERE `id`='" + entity.getId() + "';";
    }

    @Override
    protected String getSaveQuery(GuildMember entity) {
        return StringUtils.format("UPDATE `guild_members` SET `guild`='{0}', `member`='{1}', `rank`='{2}', `rights`='{3}', `experienceRate`='{4}', `experienceGiven`='{5}' WHERE `id`='{6}';",
                entity.getGuild().getId(),
                entity.getMember().getId(),
                entity.getRank(),
                entity.getRights().toInt(),
                entity.getExperienceRate(),
                entity.getExperienceGiven(),
                entity.getId()
        );
    }

    @Override
    protected void beforeLoading() throws LoadingException {
        super.beforeLoading();

        if (!guilds.isLoaded()){
            throw new LoadingException("GuildRepository is not loaded.");
        }
        if (!characters.isLoaded()){
            throw new LoadingException("CharacterRepository is not loaded.");
        }
    }

    @Override
    protected void afterLoading() {
        super.afterLoading();

        for (GuildMember member : entities.values()){
            if (nextId < member.getId()){
                nextId = member.getId();
            }
        }
    }

    @Override
    protected String getLoadQuery() {
        return "SELECT * FROM `guild_members`;";
    }

    @Override
    protected String getLoadOneQuery(Long id) {
        return "SELECT * FROM `guild_members` WHERE `id`='" + id + "';";
    }

    @Override
    protected GuildMember loadOne(ResultSet reader) throws SQLException {
        GuildMember member = new GuildMember(
                reader.getLong("id"),
                guilds.findById(reader.getInt("guild")),
                characters.findById(reader.getLong("member")),
                reader.getInt("rank"),
                new RightList(reader.getInt("rights")),
                reader.getByte("experienceRate"),
                reader.getLong("experienceGiven")
        );

        member.getGuild().addMember(member);
        member.getMember().setGuildData(new GuildData(member.getGuild(), member));

        return member;
    }
}
