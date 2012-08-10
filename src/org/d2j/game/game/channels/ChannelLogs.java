package org.d2j.game.game.channels;

import org.d2j.common.client.protocol.enums.ChannelEnum;
import org.d2j.game.configuration.IGameConfiguration;

import java.util.HashMap;

/**
 * User: Blackrush
 * Date: 22/12/11
 * Time: 13:04
 * IDE : IntelliJ IDEA
 */
public class ChannelLogs extends HashMap<ChannelEnum, Long> {
    private final IGameConfiguration configuration;

    public ChannelLogs(IGameConfiguration configuration) {
        this.configuration = configuration;
    }

    public void set(ChannelEnum channel) {
        if (containsKey(channel)){
            remove(channel);
        }
        put(channel, System.currentTimeMillis());
    }

    public int getDuration(ChannelEnum channel) {
        Long time = get(channel);
        if (time == null){
            return Integer.MAX_VALUE;
        }
        else{
            return (int) (System.currentTimeMillis() - time) / 1000;
        }
    }

    public boolean isValid(ChannelEnum channel){
        return getDuration(channel) >= configuration.getChannelRestrictions().get(channel);
    }

    public int getRemaining(ChannelEnum channel, IGameConfiguration config) {
        Long time = get(channel);
        if (time == null){
            return Integer.MAX_VALUE;
        }
        else{
            long allowed = time + config.getChannelRestrictions().get(channel) * 1000;
            return (int) (allowed - System.currentTimeMillis()) / 1000 + 1;
        }
    }
}
