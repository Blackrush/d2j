package org.d2j.game.repository;

import org.d2j.common.GuildEmblem;
import org.d2j.common.StringUtils;
import org.d2j.game.game.GuildExperience;
import org.d2j.game.game.guilds.GuildData;
import org.d2j.game.model.*;
import org.d2j.game.model.Character;
import org.d2j.utils.database.EntitiesContext;
import org.d2j.utils.database.LazyLoadEntity;
import org.d2j.utils.database.annotation.Dynamic;
import org.d2j.utils.database.repository.AbstractEntityRepository;
import org.d2j.utils.database.repository.IBaseEntityRepository;
import org.d2j.utils.database.repository.IEntityRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 04/02/12
 * Time: 10:26
 * To change this template use File | Settings | File Templates.
 */
@Singleton
public class GuildRepository extends AbstractEntityRepository<Guild, Integer> {
    private final IEntityRepository<GuildMember, Long> members;
    private final IBaseEntityRepository<ExperienceTemplate, Short> experienceTemplates;

    private int nextId;

    @Inject
    protected GuildRepository(@Dynamic EntitiesContext context, IEntityRepository<GuildMember, Long> members, IBaseEntityRepository<ExperienceTemplate, Short> experienceTemplates) {
        super(context);
        this.members = members;
        this.experienceTemplates = experienceTemplates;
    }

    public boolean exists(String name){
        for (Guild guild : entities.values()){
            if (name.equalsIgnoreCase(guild.getName())){
                return true;
            }
        }
        return false;
    }

    public boolean exists(GuildEmblem emblem){
        for (Guild guild : entities.values()){
            if (emblem.equals(guild.getEmblem())){
                return true;
            }
        }
        return false;
    }

    public Guild createDefault(Character leader, String name, GuildEmblem emblem) {
        Guild guild = new Guild(name, new GuildExperience((short) 1, 0, experienceTemplates), emblem, members);
        GuildMember member = guild.addMember(leader);

        leader.setGuildData(new GuildData(guild, member));
        guild.setLeader(member);

        create(guild);
        members.create(member);
        save(guild);

        return guild;
    }

    @Override
    protected void setNextId(Guild entity) {
        entity.setId(++nextId);
    }

    @Override
    protected String getCreateQuery(Guild entity) {
        return StringUtils.format("INSERT INTO `guilds`(`id`,`name`,`level`,`experience`,`backgroundId`,`backgroundColor`,`foregroundId`,`foregroundColor`,`leader`) VALUES('{0}','{1}','{2}','{3}','{4}','{5}','{6}','{7}','{8}');",
                entity.getId(),
                entity.getName(),
                entity.getExperience().getLevel(),
                entity.getExperience().getExperience(),
                entity.getEmblem().getBackgroundId(),
                entity.getEmblem().getBackgroundColor(),
                entity.getEmblem().getForegroundId(),
                entity.getEmblem().getForegroundColor(),
                entity.getLeader().getId()
        );
    }

    @Override
    protected String getDeleteQuery(Guild entity) {
        return "DELETE FROM `guilds` WHERE `id`='" + entity.getId() + "';";
    }

    @Override
    protected String getSaveQuery(Guild entity) {
        return StringUtils.format("UPDATE `guilds` SET `name`='{0}', `level`='{1}', `experience`='{2}', `backgroundId`='{3}', `backgroundColor`='{4}', `foregroundId`='{5}', `foregroundColor`='{6}', `leader`='{7}' WHERE `id`='{8}';",
                entity.getName(),
                entity.getExperience().getLevel(),
                entity.getExperience().getExperience(),
                entity.getEmblem().getBackgroundId(),
                entity.getEmblem().getBackgroundColor(),
                entity.getEmblem().getForegroundId(),
                entity.getEmblem().getForegroundColor(),
                entity.getLeader().getId(),
                entity.getId()
        );
    }

    @Override
    protected void afterLoading() {
        super.afterLoading();

        for (Guild guild : entities.values()){
            if (nextId < guild.getId()){
                nextId = guild.getId();
            }
        }
    }

    @Override
    protected String getLoadQuery() {
        return "SELECT * FROM `guilds`;";
    }

    @Override
    protected String getLoadOneQuery(Integer id) {
        return "SELECT * FROM `guilds` WHERE `id`='" + id + "';";
    }

    @Override
    protected Guild loadOne(ResultSet reader) throws SQLException {
        return new Guild(
                reader.getInt("id"),
                reader.getString("name"),
                new GuildExperience(
                        reader.getShort("level"),
                        reader.getShort("experience"),
                        experienceTemplates
                ),
                new GuildEmblem(
                        reader.getInt("backgroundId"),
                        reader.getInt("backgroundColor"),
                        reader.getInt("foregroundId"),
                        reader.getInt("foregroundColor")
                ),
                new LazyLoadEntity<>(members, reader.getLong("leader")),
                members
        );
    }
}
