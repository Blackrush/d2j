package org.d2j.login.repository;

import org.d2j.common.Permissions;
import org.d2j.common.StringUtils;
import org.d2j.login.configuration.ILoginConfiguration;
import org.d2j.login.model.LoginAccount;
import org.d2j.utils.database.EntitiesContext;
import org.d2j.utils.database.repository.AbstractRefreshableEntityRepository;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * User: Blackrush
 * Date: 30/10/11
 * Time: 09:29
 * IDE : IntelliJ IDEA
 */
@Singleton
public class AccountRepository extends AbstractRefreshableEntityRepository<LoginAccount, Integer> {
    @Inject
    public AccountRepository(EntitiesContext context, ILoginConfiguration configuration) {
        super(context, configuration.getRefreshInterval(), LoggerFactory.getLogger(AccountRepository.class));
    }

    @Override
    protected String getRefreshQuery() {
        return "SELECT * FROM `accounts` WHERE `refreshNeeded`='1';";
    }

    @Override
    protected String getRefreshedQuery() {
        return "UPDATE `accounts` SET `refreshNeeded`='0';";
    }

    @Override
    protected Integer readId(ResultSet reader) throws SQLException {
        return reader.getInt("id");
    }

    @Override
    protected void refresh(LoginAccount entity, ResultSet reader) throws SQLException {
        entity.setPassword(reader.getString("password"));
        entity.setRights(Permissions.valueOf(reader.getInt("rights")));
    }

    @Override
    protected String getSaveQuery(LoginAccount entity) {
        return StringUtils.format(
                "UPDATE `accounts` SET " +
                        "`rights` = '{0}' " +
                        "WHERE `id`='{1}';",
                entity.getRights().ordinal(),

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
    protected LoginAccount loadOne(ResultSet reader) throws SQLException {
        return new LoginAccount(
                reader.getInt("id"),
                reader.getString("name"),
                reader.getString("password"),
                reader.getString("nickname"),
                reader.getString("question"),
                reader.getString("answer"),
                Permissions.valueOf(reader.getInt("rights")),
                reader.getInt("community")
        );
    }

    public LoginAccount findByName(String name){
        for (LoginAccount entity : entities.values()){
            if (name.equalsIgnoreCase(entity.getName()))
                return entity;
        }
        return null;
    }
}
