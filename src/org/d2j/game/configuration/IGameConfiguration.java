package org.d2j.game.configuration;

import org.d2j.common.client.protocol.enums.ChannelEnum;
import org.d2j.game.game.channels.ChannelList;
import org.d2j.utils.database.ConnectionStringBuilder;
import org.joda.time.Duration;

import javax.script.ScriptEngine;
import java.util.HashMap;

/**
 * User: Blackrush
 * Date: 31/10/11
 * Time: 13:19
 * IDE : IntelliJ IDEA
 */
public interface IGameConfiguration {
    boolean getDebugMode();

    String getRemoteAddress();  // for clients
    int getRemotePort();        // for clients
    String getSystemAddress();  // for Loginserver
    int getSystemPort();        // for Loginserver

    ConnectionStringBuilder getDynamicConnectionInformations();
    ConnectionStringBuilder getStaticConnectionInformations();
    int getSaveDatabaseInterval();
    int getExecutionInterval();

    int getServerId();
    boolean getCharacterNameSuggestionEnabled();
    short getMaxCharactersPerAccount();
    int getDeletionAnswerRequiredLevel();

    short getStartSize();
    short getStartLevel();
    short getStartEnergy();
    int getStartKamas();
    Integer getStartMapId();
    short getStartCellId();
    boolean getAddAllWaypoints();

    short getSpellStartLevel();
    short getMaxLevelSpell();
    short getStatsPointsPerLevel();
    short getSpellsPointsPerLevel();

    Duration getTurnDuration();

    char getCommandPrefix();
    boolean getCommandEnabled();

    String getMotd();
    int getPubInterval();// minutes
    String[] getPubMessages();
    String getPubColor();

    HashMap<ChannelEnum, Integer> getChannelRestrictions();
    ChannelList getDefaultChannelList();

    ScriptEngine getScriptEngine();

    int getMaxFriends();
    int getMaxEnnemies();

    int getMaxGuildMembers();
}
