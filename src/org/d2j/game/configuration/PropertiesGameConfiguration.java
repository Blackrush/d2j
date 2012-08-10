package org.d2j.game.configuration;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.d2j.common.client.protocol.enums.ChannelEnum;
import org.d2j.game.game.channels.ChannelList;
import org.d2j.utils.D2jScriptEngineFactory;
import org.d2j.utils.Maker;
import org.d2j.utils.database.ConnectionStringBuilder;
import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import javax.script.ScriptEngine;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 16/02/12
 * Time: 18:43
 * To change this template use File | Settings | File Templates.
 */
@Singleton
public class PropertiesGameConfiguration extends PropertiesConfiguration implements IGameConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(PropertiesGameConfiguration.class);
    private static final String FILE_NAME = System.getProperty("user.dir") + "/game.properties";

    private static HashMap<ChannelEnum, Integer> getChannelRestrictions(Configuration configuration, String name){
        HashMap<ChannelEnum, Integer> result = new HashMap<>();
        for (ChannelEnum channel : ChannelEnum.values()){
            result.put(channel, configuration.getInt(name + "." + channel.name(), 0));
        }
        return result;
    }

    private static ChannelList getDefaultChannelList(Configuration configuration, String name){
        ChannelList channels = new ChannelList();
        for (String channelName : configuration.getStringArray(name)){
            channels.add(ChannelEnum.valueOf(channelName));
        }
        return channels;
    }

    private boolean debugMode;
    private String remoteAddress, systemAddress;
    private int remotePort, systemPort;
    private ConnectionStringBuilder dynamicConnectionInformations, staticConnectionInformations;
    private int saveDatabaseInterval, executionInterval;
    private int serverId;
    private boolean characterNameSuggestionEnabled;
    private short maxCharactersPerAccount;
    private int deletionAnswerRequiredLevel;
    private short startSize, startLevel, startEnergy;
    private int startKamas;
    private int startMapId;
    private short startCellId;
    private boolean addAllWaypoints;
    private short spellStartLevel, maxLevelSpell;
    private short statsPointsPerLevel, spellsPointsPerLevel;
    private Duration turnDuration;
    private char commandPrefix;
    private boolean commandEnabled;
    private String motd;
    private int pubInterval;
    private String[] pubMessages;
    private String pubColor;
    private HashMap<ChannelEnum, Integer> channelRestrictions;
    private ChannelList defaultChannelList;
    private Maker<ScriptEngine> scriptEngineFactory;
    private int maxFriends, maxEnnemies;
    private int maxGuildMembers;

    public PropertiesGameConfiguration() throws ConfigurationException {
        super(FILE_NAME);

        debugMode = getBoolean("server.debug", false);
        remoteAddress = getString("server.address.remote");
        systemAddress = getString("server.address.system");
        remotePort = getInt("server.port.remote");
        systemPort = getInt("server.port.system");
        dynamicConnectionInformations = ConnectionStringBuilder.loadFromConfiguration(this, "mysql.dynamic.");
        staticConnectionInformations = ConnectionStringBuilder.loadFromConfiguration(this, "mysql.static.");
        saveDatabaseInterval = getInt("mysql.dynamic.save.interval");
        executionInterval = getInt("mysql.dynamic.execution.interval");
        serverId = getInt("server.id");
        characterNameSuggestionEnabled = getBoolean("characters.name.suggestion");
        maxCharactersPerAccount = getShort("accounts.characters.max");
        deletionAnswerRequiredLevel = getShort("accounts.characters.delete.minlevel");
        startSize = getShort("characters.start.size");
        startLevel = getShort("characters.start.level");
        startEnergy = getShort("characters.start.energy");
        startKamas = getInt("characters.start.kamas");
        startMapId = getInt("characters.start.map");
        startCellId = getShort("characters.start.cell");
        spellStartLevel = getShort("spells.level.start");
        addAllWaypoints = getBoolean("characters.waypoints.all");
        maxLevelSpell = getShort("spells.level.max");
        statsPointsPerLevel = getShort("characters.points.stats.perlevel");
        spellsPointsPerLevel = getShort("characters.points.spells.perlevel");
        turnDuration = new Duration(getInt("fights.turns.duration") * 1000);
        commandPrefix = getString("commands.prefix").charAt(0);
        commandEnabled = getBoolean("commands.enabled");
        motd = getString("server.motd");
        pubInterval = getInt("server.pub.interval");
        pubMessages = getStringArray("server.pub.messages");
        pubColor = getString("server.pub.color");
        channelRestrictions = getChannelRestrictions(this, "server.channels.restrictions");
        defaultChannelList = getDefaultChannelList(this, "server.channels.start");
        scriptEngineFactory = new D2jScriptEngineFactory(getString("server.script"));
        maxFriends = getInt("friends.max");
        maxEnnemies = getInt("ennemies.max");
        maxGuildMembers = getInt("guilds.members.max");

        logger.info("successfully loaded from {}.", FILE_NAME);
    }

    @Override
    public boolean getDebugMode() {
        return debugMode;
    }

    @Override
    public String getRemoteAddress() {
        return remoteAddress;
    }

    @Override
    public int getRemotePort() {
        return remotePort;
    }

    @Override
    public String getSystemAddress() {
        return systemAddress;
    }

    @Override
    public int getSystemPort() {
        return systemPort;
    }

    @Override
    public ConnectionStringBuilder getDynamicConnectionInformations() {
        return dynamicConnectionInformations;
    }

    @Override
    public ConnectionStringBuilder getStaticConnectionInformations() {
        return staticConnectionInformations;
    }

    @Override
    public int getSaveDatabaseInterval() {
        return saveDatabaseInterval;
    }

    @Override
    public int getExecutionInterval() {
        return executionInterval;
    }

    @Override
    public int getServerId() {
        return serverId;
    }

    @Override
    public boolean getCharacterNameSuggestionEnabled() {
        return characterNameSuggestionEnabled;
    }

    @Override
    public short getMaxCharactersPerAccount() {
        return maxCharactersPerAccount;
    }

    @Override
    public int getDeletionAnswerRequiredLevel() {
        return deletionAnswerRequiredLevel;
    }

    @Override
    public short getStartSize() {
        return startSize;
    }

    @Override
    public short getStartLevel() {
        return startLevel;
    }

    @Override
    public short getStartEnergy() {
        return startEnergy;
    }

    @Override
    public int getStartKamas() {
        return startKamas;
    }

    @Override
    public Integer getStartMapId() {
        return startMapId;
    }

    @Override
    public short getStartCellId() {
        return startCellId;
    }

    @Override
    public boolean getAddAllWaypoints() {
        return addAllWaypoints;
    }

    @Override
    public short getSpellStartLevel() {
        return spellStartLevel;
    }

    @Override
    public short getMaxLevelSpell() {
        return maxLevelSpell;
    }

    @Override
    public short getStatsPointsPerLevel() {
        return statsPointsPerLevel;
    }

    @Override
    public short getSpellsPointsPerLevel() {
        return spellsPointsPerLevel;
    }

    @Override
    public Duration getTurnDuration() {
        return turnDuration;
    }

    @Override
    public char getCommandPrefix() {
        return commandPrefix;
    }

    @Override
    public boolean getCommandEnabled() {
        return commandEnabled;
    }

    @Override
    public String getMotd() {
        return motd;
    }

    @Override
    public int getPubInterval() {
        return pubInterval;
    }

    @Override
    public String[] getPubMessages() {
        return pubMessages;
    }

    @Override
    public String getPubColor() {
        return pubColor;
    }

    @Override
    public HashMap<ChannelEnum, Integer> getChannelRestrictions() {
        return channelRestrictions;
    }

    @Override
    public ChannelList getDefaultChannelList() {
        return defaultChannelList;
    }

    @Override
    public ScriptEngine getScriptEngine() {
        return scriptEngineFactory.make();
    }

    @Override
    public int getMaxFriends() {
        return maxFriends;
    }

    @Override
    public int getMaxEnnemies() {
        return maxEnnemies;
    }

    @Override
    public int getMaxGuildMembers() {
        return maxGuildMembers;
    }
}
