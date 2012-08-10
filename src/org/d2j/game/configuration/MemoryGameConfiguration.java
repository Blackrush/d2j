package org.d2j.game.configuration;

import org.d2j.common.client.protocol.enums.ChannelEnum;
import org.d2j.game.game.channels.ChannelList;
import org.d2j.game.game.statistics.CharacterStatistics;
import org.d2j.utils.database.ConnectionStringBuilder;
import org.joda.time.Duration;

import javax.inject.Singleton;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 * User: Blackrush
 * Date: 31/10/11
 * Time: 16:09
 * IDE : IntelliJ IDEA
 */
@Singleton
public class MemoryGameConfiguration implements IGameConfiguration {
    @Override
    public boolean getDebugMode() {
        return true;
    }

    public String getRemoteAddress() {
        return "89.92.233.236";
    }

    public int getRemotePort() {
        return 5555;
    }

    public String getSystemAddress() {
        return "127.0.0.1";
    }

    public int getSystemPort() {
        return 4443;
    }

    public ConnectionStringBuilder getDynamicConnectionInformations() {
        return new ConnectionStringBuilder("localhost", "d2j_game1", "root", "");
    }

    public ConnectionStringBuilder getStaticConnectionInformations() {
        return new ConnectionStringBuilder("localhost", "d2j_static", "root", "");
    }

    @Override
    public int getSaveDatabaseInterval() {
        return 7 * 60;
    }

    @Override
    public int getExecutionInterval() {
        return 3 * 60;
    }

    public int getServerId() {
        return 1;
    }

    public boolean getCharacterNameSuggestionEnabled() {
        return true;
    }

    public short getMaxCharactersPerAccount() {
        return 6;
    }

    public int getDeletionAnswerRequiredLevel() {
        return 10;
    }

    public short getStartSize() {
        return 100;
    }

    public short getStartLevel() {
        return 200;
    }

    public short getStartEnergy() {
        return CharacterStatistics.MAX_ENERGY;
    }

    public int getStartKamas() {
        return 1000000;
    }

    @Override
    public Integer getStartMapId() {
        return 7411;
    }

    @Override
    public short getStartCellId() {
        return 335;
    }

    @Override
    public boolean getAddAllWaypoints() {
        return true;
    }

    @Override
    public Duration getTurnDuration() {
        return new Duration(29 * 1000);
    }

    @Override
    public short getSpellStartLevel() {
        return 1;
    }

    @Override
    public short getMaxLevelSpell() {
        return 6;
    }

    @Override
    public short getStatsPointsPerLevel() {
        return 5;
    }

    @Override
    public short getSpellsPointsPerLevel() {
        return 1;
    }

    @Override
    public char getCommandPrefix() {
        return '!';
    }

    @Override
    public boolean getCommandEnabled() {
        return true;
    }

    @Override
    public String getMotd() {
        return "Bienvenue sur <u>Hélios</u>, le serveur Dofus anka'like.";
    }

    @Override
    public int getPubInterval() {
        return 5;
    }

    @Override
    public String[] getPubMessages() {
        return new String[]{
                "Visitez notre <u><a href=\"http://www.google.fr/\">site officiel</a></u> pour " +
                "suivre toute l'actualité du serveur, échanger avec nous sur le forum et obtenir " +
                "des cadeaux en votant pour notre serveur !",

                "Message de pub olol.",

                "Visitez notre partenaire <u><a href=\"http://ankafriend.fr.nf/\">AnkaFriend</a></u>, " +
                "le forum d'émulation Dofus 1.29 !"
        };
    }

    @Override
    public String getPubColor() {
        return "C52BFF";
    }

    @Override
    public HashMap<ChannelEnum, Integer> getChannelRestrictions() {
        HashMap<ChannelEnum, Integer> restrictions = new HashMap<>();

        restrictions.put(ChannelEnum.Trade, 45);
        restrictions.put(ChannelEnum.Recruitment, 30);
        restrictions.put(ChannelEnum.Alignment, 15);

        return restrictions;
    }

    @Override
    public ChannelList getDefaultChannelList() {
        ChannelList channel = new ChannelList();

        channel.add(ChannelEnum.Information);
        channel.add(ChannelEnum.General);
        channel.add(ChannelEnum.Team);
        channel.add(ChannelEnum.Party);
        channel.add(ChannelEnum.UNKNOWN0);
        channel.add(ChannelEnum.Guild);

        return channel;
    }

    private AtomicReference<ScriptEngineManager> scriptEngineManager = new AtomicReference<>(new ScriptEngineManager());

    @Override
    public ScriptEngine getScriptEngine() {
        return scriptEngineManager.get().getEngineByExtension("js");
    }

    @Override
    public int getMaxFriends() {
        return 100;
    }

    @Override
    public int getMaxEnnemies() {
        return 50;
    }

    @Override
    public int getMaxGuildMembers() {
        return 40;
    }
}
