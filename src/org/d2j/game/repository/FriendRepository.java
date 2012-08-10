package org.d2j.game.repository;

import org.d2j.game.game.friends.FriendType;
import org.d2j.game.model.Friend;
import org.d2j.game.model.GameAccount;
import org.d2j.utils.database.EntitiesContext;
import org.d2j.utils.database.LoadingException;
import org.d2j.utils.database.annotation.Dynamic;
import org.d2j.utils.database.repository.AbstractEntityRepository;
import org.d2j.utils.database.repository.IBaseEntityRepository;
import org.d2j.utils.database.repository.IEntityRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.d2j.common.StringUtils.format;

/**
 * Created by IntelliJ IDEA.
 * User: blackrush
 * Date: 31/12/11
 * Time: 13:02
 * To change this template use File | Settings | File Templates.
 */
@Singleton
public class FriendRepository extends AbstractEntityRepository<Friend, Long> {
    private final IBaseEntityRepository<GameAccount, Integer> accounts;

    private long nextId;

    @Inject
    protected FriendRepository(@Dynamic EntitiesContext context, IEntityRepository<GameAccount, Integer> accounts) {
        super(context);
        this.accounts = accounts;
    }

    @Override
    protected void setNextId(Friend entity) {
        entity.setId(++nextId);
    }

    @Override
    protected String getCreateQuery(Friend entity) {
        return format(
                "INSERT INTO `friends`(`id`,`owner`,`target`,`type`) VALUES(" +
                "'{0}', '{1}', '{2}', '{3}'" +
                ");",
                entity.getId(),
                entity.getOwner().getId(),
                entity.getTarget().getId(),
                entity.getType().ordinal()
        );
    }

    @Override
    protected String getDeleteQuery(Friend entity) {
        return "DELETE FROM `friends` WHERE `id`='" + entity.getId() + "';";
    }

    @Override
    protected String getSaveQuery(Friend entity) {
        return null;
    }

    @Override
    protected String getLoadQuery() {
        return "SELECT * FROM `friends`;";
    }

    @Override
    protected String getLoadOneQuery(Long id) {
        return "SELECT * FROM `friends` WHERE `id`='" + id + "';";
    }

    @Override
    protected Friend loadOne(ResultSet reader) throws SQLException {
        Friend friend = new Friend(
                reader.getLong("id"),
                accounts.findById(reader.getInt("owner")),
                accounts.findById(reader.getInt("target")),
                FriendType.valueOf(reader.getInt("type"))
        );
        friend.getOwner().getFriends().addData(friend);

        return friend;
    }

    @Override
    protected void beforeLoading() throws LoadingException {
        super.beforeLoading();

        if (!accounts.isLoaded()){
            throw new LoadingException("AccountRepository isn't loaded.");
        }
    }

    @Override
    protected void afterLoading() {
        super.afterLoading();

        for (Friend value : entities.values()){
            if (nextId < value.getId()){
                nextId = value.getId();
            }
        }
    }
}
