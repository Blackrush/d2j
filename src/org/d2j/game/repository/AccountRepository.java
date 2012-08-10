package org.d2j.game.repository;

import org.d2j.common.Permissions;
import org.d2j.common.StringUtils;
import org.d2j.common.client.protocol.enums.ChannelEnum;
import org.d2j.game.configuration.IGameConfiguration;
import org.d2j.game.game.channels.ChannelList;
import org.d2j.game.game.friends.FriendList;
import org.d2j.game.model.Friend;
import org.d2j.game.model.GameAccount;
import org.d2j.utils.database.EntitiesContext;
import org.d2j.utils.database.annotation.Dynamic;
import org.d2j.utils.database.repository.AbstractEntityRepository;
import org.d2j.utils.database.repository.IEntityRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * User: Blackrush
 * Date: 31/10/11
 * Time: 14:44
 * IDE : IntelliJ IDEA
 */
@Singleton
public class AccountRepository extends AbstractEntityRepository<GameAccount, Integer> {
    protected final Map<String, Integer> tickets = new HashMap<>();

    private final IEntityRepository<Friend, Long> friends;
    private final IGameConfiguration configuration;

    public GameAccount createDefault(Integer id, String nickname, String answer, Permissions rights, int community){
        GameAccount account = new GameAccount(
                id,
                nickname,
                answer,
                rights,
                community,
                configuration.getDefaultChannelList(),
                new Date(),
                "",
                false,
                false
        );
        account.setFriends(new FriendList(friends, account, configuration));

        if (account.hasRights()){
            account.getEnabledChannels().add(ChannelEnum.Admin);
            account.getEnabledChannels().add(ChannelEnum.UNKNOWN2);
        }

        create(account);

        return account;
    }

    @Inject
    public AccountRepository(@Dynamic EntitiesContext context, IEntityRepository<Friend, Long> friends, IGameConfiguration configuration) {
        super(context);
        this.friends = friends;
        this.configuration = configuration;
    }

    @Override
    protected void setNextId(GameAccount entity) {

    }

    @Override
    protected String getCreateQuery(GameAccount entity) {
        return StringUtils.format(
                "INSERT INTO `accounts`(`id`, `nickname`, `answer`, `rights`, `community`) " +
                        "VALUES('{0}', '{1}', '{2}', '{3}', '{4}');",
                entity.getId(),
                entity.getNickname(),
                entity.getAnswer(),
                entity.getRights().ordinal(),
                entity.getCommunity()
        );
    }

    @Override
    protected String getDeleteQuery(GameAccount entity) {
        return "DELETE FROM `accounts` WHERE `id`='" + entity.getId() + "';";
    }

    @Override
    protected String getSaveQuery(GameAccount entity) {
        return StringUtils.format(
                "UPDATE `accounts` SET " +
                        "`enabledChannels`='{0}', " +
                        "`lastConnection`='{1}', " +
                        "`lastAddress`='{2}', " +
                        "`muted`='{3}', " +
                        "`notifyFriendsOnConnect`='{4}' " +
                        " WHERE `id`='{5}';",

                entity.getEnabledChannels().toString(),
                StringUtils.MYSQL_DATETIME_FORMATER.format(entity.getLastConnection()),
                entity.getLastAddress(),
                entity.isMuted() ? "1" : "0",
                entity.isNotifyFriendsOnConnect() ? "1" : "0",

                entity.getId()
        );
    }

    @Override
    protected String getLoadQuery() {
        return "SELECT * FROM `accounts`;";
    }

    @Override
    protected String getLoadOneQuery(Integer id) {
        return "SELECT * FROM `accounts` WHERE `id`='" + id + "';";
    }

    @Override
    protected GameAccount loadOne(ResultSet reader) throws SQLException {
        try {
            GameAccount account = new GameAccount(
                    reader.getInt("id"),
                    reader.getString("nickname"),
                    reader.getString("answer"),
                    Permissions.valueOf(reader.getInt("rights")),
                    reader.getInt("community"),
                    ChannelList.parse(reader.getString("enabledChannels")),
                    StringUtils.MYSQL_DATETIME_FORMATER.parse(reader.getString("lastConnection")),
                    reader.getString("lastAddress"),
                    reader.getBoolean("muted"),
                    reader.getBoolean("notifyFriendsOnConnect")
            );
            account.setFriends(new FriendList(friends, account, configuration));

            return account;
        } catch (ParseException e) {
            throw new SQLException(e.getMessage(), e.getCause());
        }
    }

    public void addTicket(String ticket, Integer accountId){
        tickets.put(ticket, accountId);
    }

    public GameAccount findByTicket(String ticket){
        Integer accountId = tickets.get(ticket);

        if (accountId == null)
            return null;
        else
            tickets.remove(ticket);

        return entities.get(accountId);
    }

    public GameAccount findByNickname(String nickname){
        for (GameAccount account : entities.values()){
            if (nickname.equals(account.getNickname())){
                return account;
            }
        }
        return null;
    }

    public GameAccount findByIdOrNickname(String val){
        try {
            return findById(Integer.parseInt(val));
        }
        catch (NumberFormatException e){
            return findByNickname(val);
        }
    }

    public Map<Integer, Integer> map() {
        Map<Integer, Integer> result = new HashMap<>(entities.size());
        for (GameAccount account : entities.values()){
            result.put(account.getId(), account.getCharacters().size());
        }
        return result;
    }
}
