package org.d2j.game.game.commands;

import org.d2j.common.Permissions;
import org.d2j.game.game.logging.DofusLogger;
import org.d2j.game.service.IWorld;
import org.d2j.game.service.game.GameClient;
import org.d2j.utils.DelegateTimerCacheSystem;
import org.d2j.utils.Maker;
import org.joda.time.Duration;

import static org.d2j.common.StringUtils.format;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 21/01/12
 * Time: 11:50
 * To change this template use File | Settings | File Templates.
 */
public class InfosCommand extends AbstractCommand implements Maker<String> {
    private final IWorld world;
    private final DelegateTimerCacheSystem<String> messageCache;

    public InfosCommand(IWorld world) {
        this.world = world;
        this.messageCache = new DelegateTimerCacheSystem<>(this, new Duration(5 * 60 * 1000));
    }

    @Override
    public String helpMessage() {
        return "Show server's informations.";
    }

    @Override
    public String[] usageParameters() {
        return new String[0];
    }

    @Override
    public String name() {
        return "infos";
    }

    @Override
    public Permissions level() {
        return Permissions.MEMBER;
    }

    @Override
    public void parse(GameClient client, DofusLogger out, String[] args) {
        out.info(messageCache.get());
    }

    @Override
    public String make() {
        Duration uptime = world.uptime();

        return format("There are {0} staff's members online and {1} players online." +
                      "The simulateneous players count's record is {2}.\n" +
                      "Uptime : {3} days {4} hours and {5} minutes.\n" +
                      "===============================",
                world.getOnlineStaff().size(),
                world.getOnlinePlayers().size(),
                world.getGameService().getRecordConnected(),
                uptime.getStandardDays(),
                uptime.getStandardHours(),
                uptime.getStandardMinutes()
        );
    }
}
